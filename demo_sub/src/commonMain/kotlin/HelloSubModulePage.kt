package test

import com.tencent.kuikly.core.annotations.Page
import com.tencent.kuikly.core.base.Color
import com.tencent.kuikly.core.base.ComposeEvent
import com.tencent.kuikly.core.base.ViewBuilder
import com.tencent.kuikly.core.pager.Pager
import com.tencent.kuikly.core.views.RichText
import com.tencent.kuikly.core.views.Span
import com.tencent.kuikly.core.views.View

@Page("HelloSubModulePage")
internal class HelloSubModulePage : Pager() {

    override fun createEvent(): ComposeEvent {
        return ComposeEvent()
    }

    override fun body(): ViewBuilder {
        return {
            attr {
                backgroundColor(Color.WHITE)
                flexDirectionColumn()
                alignItemsCenter()
                padding(16f)
            }

            RichText {
                attr {
                    marginTop(30f)
                    color(Color.BLACK)
                    fontSize(24f)
                }
                Span {
                    text("Hello from Sub Module!")
                }
            }

            View {
                attr {
                    height(16f)
                }
            }

            RichText {
                attr {
                    color(Color.GRAY)
                    fontSize(16f)
                }
                Span {
                    text("This is a page defined in demo_sub module.")
                }
            }
        }
    }
}
