/*
 * Tencent is pleased to support the open source community by making KuiklyUI
 * available.
 * Copyright (C) 2025 Tencent. All rights reserved.
 * Licensed under the License of KuiklyUI;
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * https://github.com/Tencent-TDS/KuiklyUI/blob/main/LICENSE
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.tencent.kuikly.demo.pages.demo

import com.tencent.kuikly.core.annotations.Page
import com.tencent.kuikly.core.base.Color
import com.tencent.kuikly.core.base.ViewBuilder
import com.tencent.kuikly.core.directives.vfor
import com.tencent.kuikly.core.log.KLog
import com.tencent.kuikly.core.reactive.handler.observableList
import com.tencent.kuikly.core.timer.setTimeout
import com.tencent.kuikly.core.views.PageList
import com.tencent.kuikly.core.views.Text
import com.tencent.kuikly.core.views.View
import com.tencent.kuikly.core.views.WaterfallList
import com.tencent.kuikly.demo.pages.base.BasePager
import com.tencent.kuikly.demo.pages.demo.base.NavBar

/**
 * Reproduce the nested scrolling async rendering bug:
 * PageList (outer scrolling container, needCustomWrapper=true)
 *   └── vfor(asyncOuterList)  <-- outer data is ASYNC (simulating network fetch)
 *         └── WaterfallList (inner scrolling container, needCustomWrapper=true)
 *               └── vforIndex(asyncInnerList)  <-- inner items also ASYNC
 *
 * Bug: Inner custom-wrapper is created but remains empty because:
 * 1. Outer vfor data arrives async → PageList children created late
 * 2. Inner WaterfallList's custom-wrapper hasn't completed `attached` lifecycle
 * 3. When inner items are added, findCustomWrapper routes to inner wrapper ID
 * 4. But customWrapperCache.get(innerWrapperId) returns undefined → setData skipped
 * 5. Inner content never renders
 */

internal data class AsyncTabData(var tabTitle: String = "", var index: Int = 0)

internal data class AsyncGoodsItem(var title: String = "", var price: String = "")

@Page("WaterfallAsyncIssueTest")
internal class WaterfallAsyncIssueTest : BasePager() {
    // Outer list: simulates async category/tab data from network
    private val goodsCategoryList by observableList<AsyncTabData>()
    // Inner lists: simulates async goods data per category
    private val goodsList0 by observableList<AsyncGoodsItem>()
    private val goodsList1 by observableList<AsyncGoodsItem>()
    private val goodsList2 by observableList<AsyncGoodsItem>()

    override fun created() {
        super.created()

        // KEY DIFFERENCE from WallfallIssueTest:
        // Outer tabDataList is NOT filled synchronously in created().
        // Instead, it simulates an async network request (e.g., fetch category list).
        // This causes PageList's children (including inner WaterfallList) to be created LATE,
        // so inner custom-wrapper doesn't have time to complete `attached` before inner items arrive.

        // Simulate async fetch of category list (500ms delay)
        setTimeout(500) {
            KLog.i("WaterfallAsyncIssueTest", "Async outer data arrived: goodsCategoryList")
            goodsCategoryList.addAll(
                listOf(
                    AsyncTabData("Hot Sale", 0),
                    AsyncTabData("New Arrival", 1),
                    AsyncTabData("Discount", 2)
                )
            )

            // Simulate async fetch of goods data (arrives shortly after categories)
            // In real business, this might be a separate API call or triggered after tab creation
            setTimeout(300) {
                KLog.i("WaterfallAsyncIssueTest", "Async inner data arrived: goodsList0/1/2")

                goodsList0.addAll(
                    (0..11).map { i ->
                        AsyncGoodsItem(
                            title = "Hot Item #$i - Premium Quality Product",
                            price = "¥${(i + 1) * 29}.99"
                        )
                    }
                )

                goodsList1.addAll(
                    (0..9).map { i ->
                        AsyncGoodsItem(
                            title = "New Item #$i - Latest Fashion Style",
                            price = "¥${(i + 1) * 39}.99"
                        )
                    }
                )

                goodsList2.addAll(
                    (0..7).map { i ->
                        AsyncGoodsItem(
                            title = "Sale Item #$i - Limited Time Offer",
                            price = "¥${(i + 1) * 19}.99"
                        )
                    }
                )
            }
        }
    }

    override fun body(): ViewBuilder {
        val ctx = this
        return {

            NavBar {
                attr {
                    title = "WaterfallAsyncIssueTest"
                }
            }

            // Header area (renders fine - single layer, no nesting issue)
            View {
                attr {
                    height(50f)
                    backgroundColor(Color(0xFF333333.toInt()))
                    justifyContentCenter()
                    alignItemsCenter()
                }
                Text {
                    attr {
                        fontSize(16f)
                        color(Color.WHITE)
                        text("Goods Category Tabs (Header)")
                    }
                }
            }

            // BUG AREA: PageList → vfor(asyncData) → WaterfallList → vfor(asyncItems)
            // Both PageList and WaterfallList are scrolling containers → both get needCustomWrapper=true
            // Outer vfor data is async → inner custom-wrapper created late → inner items never render
            PageList {
                attr {
                    flex(1f)
                    flexDirectionRow()
                }
                event {
                    contentSizeChanged { width, height ->
                        KLog.i("WaterfallAsyncIssueTest", "PageList contentSize: ${width}x${height}")
                    }
                }

                // Outer vfor bound to ASYNC data (goodsCategoryList filled after 500ms)
                vfor({ ctx.goodsCategoryList }) { category ->
                    WaterfallList {
                        attr {
                            flex(1f)
                            firstContentLoadMaxIndex(1000)
                            listWidth(pagerData.pageViewWidth)
                            columnCount(2)
                            itemSpacing(10f)
                            lineSpacing(10f)
                        }

                        // Category header (static content within WaterfallList)
                        View {
                            attr {
                                height(44f)
                                width(pagerData.pageViewWidth)
                                backgroundColor(
                                    when (category.index) {
                                        0 -> Color(0xFFFF6B6B.toInt())
                                        1 -> Color(0xFF4ECDC4.toInt())
                                        else -> Color(0xFFFFE66D.toInt())
                                    }
                                )
                                justifyContentCenter()
                                alignItemsCenter()
                            }
                            Text {
                                attr {
                                    fontSize(16f)
                                    color(Color.WHITE)
                                    text("${category.tabTitle} (index=${category.index})")
                                }
                            }
                        }

                        // Inner vfor bound to ASYNC goods data (filled 300ms after categories)
                        vfor({
                            when (category.index) {
                                0 -> ctx.goodsList0
                                1 -> ctx.goodsList1
                                else -> ctx.goodsList2
                            }
                        }) { goods ->
                            // Goods card cell
                            View {
                                attr {
                                    margin(5f)
                                    padding(10f)
                                    backgroundColor(Color.WHITE)
                                    borderRadius(8f)
                                }
                                Text {
                                    attr {
                                        fontSize(14f)
                                        color(Color(0xFF333333.toInt()))
                                        text(goods.title)
                                    }
                                }
                                Text {
                                    attr {
                                        fontSize(16f)
                                        color(Color(0xFFFF4444.toInt()))
                                        marginTop(6f)
                                        text(goods.price)
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
