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

package com.tencent.kuikly.demo.pages.demo.kit_demo.DeclarativeDemo

import com.tencent.kuikly.core.annotations.Page
import com.tencent.kuikly.core.base.BoxShadow
import com.tencent.kuikly.core.base.Color
import com.tencent.kuikly.core.base.ViewBuilder
import com.tencent.kuikly.core.views.Image
import com.tencent.kuikly.core.views.List
import com.tencent.kuikly.core.views.Text
import com.tencent.kuikly.core.views.View
import com.tencent.kuikly.demo.pages.base.BasePager
import com.tencent.kuikly.demo.pages.demo.base.NavBar
import com.tencent.kuikly.demo.pages.demo.kit_demo.DeclarativeDemo.Common.ViewExampleSectionHeader

@Page("ImageExamplePage")
internal class ImageExamplePage: BasePager() {
    override fun body(): ViewBuilder {
        return {
            attr { backgroundColor(Color.WHITE) }
            NavBar { attr { title = "Image Attr Example" } }
            List {
                attr { flex(1f) }
                ViewExampleSectionHeader {  attr { title = "Image { attr { resizeContain() } }" } }
                Image {
                    attr {
                        alignSelfCenter()
                        margin(all = 8f)
                        backgroundColor(0xFFE5E5E5)
                        size(width = 240f, height = 180f)
                        src("https://picsum.photos/200/300")
                        resizeContain()
                        borderRadius(20f)
                        boxShadow(BoxShadow(10f, 10f, 30f, Color.BLACK))
                    }
                }
                ViewExampleSectionHeader { attr { title = "Image { attr { resizeCover() } }" } }
                Image {
                    attr {
                        alignSelfCenter()
                        margin(all = 8f)
                        backgroundColor(0xFFE5E5E5)
                        size(width = 240f, height = 180f)
                        src("https://picsum.photos/200/300?test=1")
                        resizeCover()
                        placeholderSrc("https://vfiles.gtimg.cn/wuji_dashboard/xy/starter/59ef6918.gif")
                        boxShadow(BoxShadow(2f, 2f, 10f, Color.BLACK))
                    }
                }
                ViewExampleSectionHeader { attr { title = "Image { attr { resizeStretch() } }" } }
                Image {
                    attr {
                        alignSelfCenter()
                        margin(all = 8f)
                        backgroundColor(0xFFE5E5E5)
                        size(width = 240f, height = 180f)
                        src("https://picsum.photos/200/300")
                        resizeStretch()
                    }
                }
                ViewExampleSectionHeader { attr { title = "Image { attr { blurRadius(5f) } }" } }
                Image {
                    attr {
                        alignSelfCenter()
                        margin(all = 8f)
                        backgroundColor(0xFFE5E5E5)
                        size(width = 240f, height = 180f)
                        src("https://picsum.photos/200/300")
                        resizeCover()
                        blurRadius(5f)
                    }
                }
                // Note: On MiniApp, tintColor is implemented via CSS drop-shadow.
                // The WeChat DevTools simulator does not render this filter correctly
                // (the whole image appears as a solid color block), but it works as
                // expected on real devices (iOS / Android). Please verify on a real
                // device when testing this example.
                ViewExampleSectionHeader { attr { title = "Image { attr { tintColor(Color.RED) } }" } }
                Text {
                    attr {
                        margin(all = 8f)
                        color(Color.RED)
                        fontSize(12f)
                        text("Note: On MiniApp, please test on a real device (WeChat DevTools does not render drop-shadow correctly).")
                    }
                }
                Image {
                    attr {
                        alignSelfCenter()
                        margin(all = 8f)
                        backgroundColor(0xFFE5E5E5)
                        size(width = 180f, height = 120f)
                        src("https://raw.githubusercontent.com/Tencent-TDS/KuiklyUI/refs/heads/main/demo/src/commonMain/assets/ChatDemo/kuikly_logo.png")
                        resizeContain()
                        tintColor(Color.RED)
                    }
                }
                View {
                    attr {
                        height(3000f)
                    }
                }
            }
        }
    }
}