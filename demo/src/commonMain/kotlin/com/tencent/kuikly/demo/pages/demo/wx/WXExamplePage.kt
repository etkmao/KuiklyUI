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

package com.tencent.kuikly.demo.pages.demo.wx

import com.tencent.kuikly.core.annotations.Page
import com.tencent.kuikly.core.base.Color
import com.tencent.kuikly.core.base.ViewBuilder
import com.tencent.kuikly.core.log.KLog
import com.tencent.kuikly.core.reactive.handler.observable
import com.tencent.kuikly.core.views.List
import com.tencent.kuikly.core.views.Text
import com.tencent.kuikly.core.views.View
import com.tencent.kuikly.core.views.wx.WXButton
import com.tencent.kuikly.core.views.wx.WXButtonOpenType
import com.tencent.kuikly.core.views.wx.WXInput
import com.tencent.kuikly.core.views.wx.WXInputConfirmType
import com.tencent.kuikly.core.views.wx.WXInputType
import com.tencent.kuikly.demo.pages.base.BasePager
import com.tencent.kuikly.demo.pages.demo.base.NavBar
import com.tencent.kuikly.demo.pages.demo.kit_demo.DeclarativeDemo.Common.ViewExampleSectionHeader

@Page("WXExamplePage")
internal class WXExamplePage : BasePager() {

    private var phoneNumberTip by observable("尚未获取")
    private var userInfoTip by observable("尚未获取")
    private var inputTextTip by observable("")
    private var inputNumberTip by observable("")
    private var inputConfirmTip by observable("尚未提交")

    override fun body(): ViewBuilder {
        val ctx = this
        return {
            attr { backgroundColor(Color.WHITE) }
            NavBar { attr { title = "WX Component Demo" } }
            List {
                attr { flex(1f) }

                ViewExampleSectionHeader {
                    attr { title = "WXButton 基础样式" }
                }
                View {
                    attr {
                        flexDirectionRow()
                        alignItemsCenter()
                        justifyContentSpaceAround()
                        padding(top = 12f, bottom = 12f)
                    }
                    WXButton {
                        attr {
                            type("default")
                            size("mini")
                            titleAttr {
                                text("默认")
                                fontSize(14f)
                            }
                        }
                    }
                    WXButton {
                        attr {
                            type("primary")
                            size("mini")
                            titleAttr {
                                text("主要")
                                fontSize(14f)
                                color(Color.WHITE)
                            }
                        }
                    }
                    WXButton {
                        attr {
                            type("warn")
                            size("mini")
                            titleAttr {
                                text("警告")
                                fontSize(14f)
                                color(Color.WHITE)
                            }
                        }
                    }
                }

                ViewExampleSectionHeader {
                    attr { title = "WXButton 状态：plain / disabled / loading" }
                }
                View {
                    attr {
                        flexDirectionRow()
                        alignItemsCenter()
                        justifyContentSpaceAround()
                        padding(top = 12f, bottom = 12f)
                    }
                    WXButton {
                        attr {
                            type("primary")
                            plain(true)
                            size("mini")
                            titleAttr {
                                text("Plain")
                                fontSize(14f)
                            }
                        }
                    }
                    WXButton {
                        attr {
                            type("primary")
                            disabled(true)
                            size("mini")
                            titleAttr {
                                text("Disabled")
                                fontSize(14f)
                                color(Color.WHITE)
                            }
                        }
                    }
                    WXButton {
                        attr {
                            type("primary")
                            loading(true)
                            size("mini")
                            titleAttr {
                                text("Loading")
                                fontSize(14f)
                                color(Color.WHITE)
                            }
                        }
                    }
                }

                ViewExampleSectionHeader {
                    attr { title = "WXButton open-type：获取手机号" }
                }
                View {
                    attr {
                        flexDirectionColumn()
                        alignItemsCenter()
                        padding(top = 12f, bottom = 12f)
                    }
                    WXButton {
                        attr {
                            type("primary")
                            openType(WXButtonOpenType.GET_PHONE_NUMBER)
                            width(150f)
                            height(40f)
                            titleAttr {
                                text("获取手机号")
                                fontSize(15f)
                                color(Color.WHITE)
                            }
                        }
                        event {
                            onGetPhoneNumber { detail ->
                                KLog.i("WXExamplePage", "onGetPhoneNumber: $detail")
                                ctx.phoneNumberTip = detail.toString()
                            }
                            onError { err ->
                                KLog.e("WXExamplePage", "getPhoneNumber error: $err")
                            }
                        }
                    }
                    Text {
                        attr {
                            marginTop(8f)
                            fontSize(12f)
                            color(0xFF666666)
                            text("授权结果：${ctx.phoneNumberTip}")
                        }
                    }
                }

                ViewExampleSectionHeader {
                    attr { title = "WXButton open-type：获取用户信息" }
                }
                View {
                    attr {
                        flexDirectionColumn()
                        alignItemsCenter()
                        padding(top = 12f, bottom = 12f)
                    }
                    WXButton {
                        attr {
                            type("default")
                            openType(WXButtonOpenType.GET_USER_INFO)
                            titleAttr {
                                text("获取用户信息")
                                fontSize(15f)
                            }
                        }
                        event {
                            onGetUserInfo { detail ->
                                KLog.i("WXExamplePage", "onGetUserInfo: $detail")
                                ctx.userInfoTip = detail.toString()
                            }
                        }
                    }
                    Text {
                        attr {
                            marginTop(8f)
                            fontSize(12f)
                            color(0xFF666666)
                            text("授权结果：${ctx.userInfoTip}")
                        }
                    }
                }

                ViewExampleSectionHeader {
                    attr { title = "WXButton form-type：联系客服 / 打开设置" }
                }
                View {
                    attr {
                        flexDirectionRow()
                        alignItemsCenter()
                        justifyContentSpaceAround()
                        padding(top = 12f, bottom = 12f)
                    }
                    WXButton {
                        attr {
                            openType(WXButtonOpenType.CONTACT)
                            size("mini")
                            titleAttr {
                                text("联系客服")
                                fontSize(14f)
                            }
                        }
                        event {
                            onContact { detail ->
                                KLog.i("WXExamplePage", "onContact: $detail")
                            }
                        }
                    }
                    WXButton {
                        attr {
                            openType(WXButtonOpenType.OPEN_SETTING)
                            size("mini")
                            titleAttr {
                                text("打开设置")
                                fontSize(14f)
                            }
                        }
                        event {
                            onOpenSetting { detail ->
                                KLog.i("WXExamplePage", "onOpenSetting: $detail")
                            }
                        }
                    }
                    WXButton {
                        attr {
                            openType(WXButtonOpenType.FEEDBACK)
                            size("mini")
                            titleAttr {
                                text("意见反馈")
                                fontSize(14f)
                            }
                        }
                    }
                }

                ViewExampleSectionHeader {
                    attr { title = "WXInput 基础用法：实时输入 / 回车提交" }
                }
                View {
                    attr {
                        flexDirectionColumn()
                        padding(left = 16f, right = 16f, top = 12f, bottom = 12f)
                    }
                    WXInput {
                        attr {
                            height(40f)
                            backgroundColor(0xFFF5F5F5)
                            borderRadius(6f)
                            padding(left = 12f, right = 12f)
                            type(WXInputType.TEXT)
                            placeholder("请输入文本，回车提交")
                            confirmType(WXInputConfirmType.DONE)
                            maxLength(50)
                        }
                        event {
                            onInput { detail ->
                                KLog.i("WXExamplePage", "onInput: $detail")
                                ctx.inputTextTip = detail.optString("data")
                            }
                            onConfirm { detail ->
                                KLog.i("WXExamplePage", "onConfirm: $detail")
                                ctx.inputConfirmTip = detail.optString("data")
                            }
                            onFocus { KLog.i("WXExamplePage", "input onFocus") }
                            onBlur { KLog.i("WXExamplePage", "input onBlur") }
                        }
                    }
                    Text {
                        attr {
                            marginTop(8f)
                            fontSize(12f)
                            color(0xFF666666)
                            text("当前输入：${ctx.inputTextTip}")
                        }
                    }
                    Text {
                        attr {
                            marginTop(4f)
                            fontSize(12f)
                            color(0xFF666666)
                            text("最近一次提交：${ctx.inputConfirmTip}")
                        }
                    }
                }

                ViewExampleSectionHeader {
                    attr { title = "WXInput 数字键盘 / 密码 / 禁用" }
                }
                View {
                    attr {
                        flexDirectionColumn()
                        padding(left = 16f, right = 16f, top = 12f, bottom = 12f)
                    }
                    WXInput {
                        attr {
                            height(40f)
                            backgroundColor(0xFFF5F5F5)
                            borderRadius(6f)
                            padding(left = 12f, right = 12f)
                            type(WXInputType.NUMBER)
                            placeholder("仅可输入数字")
                        }
                        event {
                            onInput { detail ->
                                ctx.inputNumberTip = detail.optString("data")
                            }
                        }
                    }
                    Text {
                        attr {
                            marginTop(4f)
                            fontSize(12f)
                            color(0xFF666666)
                            text("数字输入：${ctx.inputNumberTip}")
                        }
                    }
                    WXInput {
                        attr {
                            marginTop(12f)
                            height(40f)
                            backgroundColor(0xFFF5F5F5)
                            borderRadius(6f)
                            padding(left = 12f, right = 12f)
                            type(WXInputType.TEXT)
                            password(true)
                            placeholder("密码输入")
                        }
                    }
                    WXInput {
                        attr {
                            marginTop(12f)
                            height(40f)
                            backgroundColor(0xFFEEEEEE)
                            borderRadius(6f)
                            padding(left = 12f, right = 12f)
                            disabled(true)
                            value("禁用状态，不可编辑")
                        }
                    }
                }
            }
        }
    }
}
