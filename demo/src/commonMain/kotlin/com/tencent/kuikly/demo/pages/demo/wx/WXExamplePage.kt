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
import com.tencent.kuikly.demo.pages.base.BasePager
import com.tencent.kuikly.demo.pages.demo.base.NavBar
import com.tencent.kuikly.demo.pages.demo.kit_demo.DeclarativeDemo.Common.ViewExampleSectionHeader

@Page("WXExamplePage")
internal class WXExamplePage : BasePager() {

    private var phoneNumberTip by observable("尚未获取")
    private var userInfoTip by observable("尚未获取")

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
            }
        }
    }
}
