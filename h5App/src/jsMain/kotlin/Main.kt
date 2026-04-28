package com.tencent.kuikly.h5app

import com.tencent.kuikly.core.render.web.expand.module.KRNotifyModule
import com.tencent.kuikly.core.render.web.processor.KuiklyProcessor
import kotlinx.browser.document
import kotlinx.browser.window
import org.w3c.dom.events.Event
import com.tencent.kuikly.h5app.manager.KuiklyRouter
import com.tencent.kuikly.h5app.processor.CustomImageProcessor
import kotlin.js.json
import kotlin.js.jsTypeOf

/**
 * WebApp entry, use renderView delegate method to initialize and create renderView
 */
fun main() {
    // Configure whether to prevent default text selection and image drag behavior.
    // Set to false to allow text selection and image dragging.
    // KuiklyProcessor.preventDefaultDragAndSelect = false

    // Takes over control if "use_spa=1" is present in URL or ENABLE_BY_DEFAULT is true
    if (KuiklyRouter.handleEntry()) {
        return
    }

    console.log("##### Kuikly H5 #####")

    // Create and initialize the page delegator using shared logic
    val delegator = KuiklyRouter.createDelegator(window.location.href)

    // modify image cdn
//    KuiklyProcessor.imageProcessor = CustomImageProcessor

    // Register visibility event
    document.addEventListener("visibilitychange", {
        val hidden = document.asDynamic().hidden as Boolean
        if (hidden) {
            // Page hidden
            delegator.pause()
        } else {
            // Page restored
            delegator.resume()
        }
    })

    // Register Kuikly event listener for Web host to receive events from Kuikly pages
    // When Kuikly page calls NotifyModule.postNotify(), Web host can receive the event here
    registerKuiklyEventListener()

    // When using custom fonts, fonts are loaded asynchronously, so a re-layout needs to be 
    // triggered after loading completes to re-measure text with the correct font metrics
    // document.asDynamic().fonts.load("16px 'Kanit Medium'").then({ _ ->
    //     delegator.fontLoaded()
    // })

    // 注册微信字体档位变化监听，触发 Kuikly 文本重测
    // 注意：这里不调用 WeixinJSBridge.invoke('setFontSizeCallback', { fontSize: 0 })，
    // 因此保留微信默认的字体缩放行为（用户在微信中调整字号后页面会缩放）。
    // val setupWxFontListener: () -> Unit = setup@{
    //     val wx = window.asDynamic().WeixinJSBridge
    //     if (jsTypeOf(wx) == "undefined" || wx == null) return@setup
    //     wx.on("menu:setfont", { e: dynamic ->
    //         console.log("[Kuikly] 微信字体档位变化 档位:", e?.fontSize, "缩放系数:", e?.fontScale)
    //         // 微信把新字号应用到 DOM 上是异步的，延后一帧再测量更稳妥
    //         window.setTimeout({
    //             delegator.fontLoaded()
    //         }, 0)
    //     })
    // }

    // // WeixinJSBridge 可能在脚本执行前就已 ready，此时不会再派发 WeixinJSBridgeReady
    // if (jsTypeOf(window.asDynamic().WeixinJSBridge) == "undefined") {
    //     document.addEventListener("WeixinJSBridgeReady", { setupWxFontListener() }, false)
    // } else {
    //     setupWxFontListener()
    // }

    // window.setTimeout({
    //     delegator.fontLoaded()
    // }, 5000)

    // 页面加载时或布局刷新前调用
    // val currentScale = getActualFontScale()
    // if (currentScale > 1) {
    //     console.warn("当前处于大字体模式，比例：", currentScale)
    //     window.setTimeout({
    //         delegator.fontLoaded()
    //     }, 0)
    // }

    window.setTimeout({
        val currentScale = getActualFontScale()
        if (currentScale > 1) {
            console.warn("当前处于大字体模式，比例：", currentScale)
            window.setTimeout({
                delegator.fontLoaded()
            }, 0)
        }
    }, 5000)
}

/**
 * 获取当前页面的实际字体缩放比例。
 * 原理：创建一个隐藏 span 设置 font-size=16px，读取浏览器实际渲染出的像素值，
 * 两者相除即得到缩放系数（例如微信调大字号后可能返回 1.25 / 1.5 等）。
 */
private fun getActualFontScale(): Double {
    val span = document.createElement("span").asDynamic()
    span.innerText = "T"
    span.style.fontSize = "16px"
    span.style.position = "absolute"
    span.style.visibility = "hidden"
    document.body?.appendChild(span as org.w3c.dom.Node)

    // 获取实际渲染出的像素值
    val computed = window.getComputedStyle(span as org.w3c.dom.Element).fontSize
    val actualSize = computed.replace("px", "").trim().toDoubleOrNull() ?: 16.0
    document.body?.removeChild(span as org.w3c.dom.Node)

    // 计算比例：实际值 / 设定值
    return actualSize / 16.0
}

/**
 * Register listener to receive events from Kuikly pages
 * 
 * Usage in Kuikly page:
 * ```kotlin
 * acquireModule<NotifyModule>(NotifyModule.MODULE_NAME)
 *     .postNotify("your_event_name", JSONObject().apply { put("key", "value") })
 * ```
 */
private fun registerKuiklyEventListener() {
    window.addEventListener("kuikly_to_host_event", { event: Event ->
        val detail = event.asDynamic().detail
        val eventName = detail.eventName as? String ?: ""
        val data = detail.data as? String ?: "{}"
        
        console.log("[Web Host] Received Kuikly event: $eventName")
        console.log("[Web Host] Event data: $data")
    })
    console.log("[Web Host] Kuikly event listener registered")
}
