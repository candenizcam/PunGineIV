package pungine

import application.GlobalAccess
import com.soywiz.korge.scene.Scene
import com.soywiz.korge.view.*
import com.soywiz.korim.bitmap.Bitmap
import com.soywiz.korim.color.Colors
import com.soywiz.korim.color.RGBA
import com.soywiz.korio.async.launchImmediately

import pungine.geometry2D.Rectangle
import pungine.geometry2D.Vector
import pungine.geometry2D.oneRectangle
import kotlin.reflect.KClass

open class PunStage(var width: Double = InternalGlobalAccess.virtualSize.width.toDouble(), var height: Double=InternalGlobalAccess.virtualSize.height.toDouble()): Scene() {
    override fun createSceneView(): Puntainer = Puntainer().also {
        scenePuntainer = it
    }
    lateinit var scenePuntainer: Puntainer // this can be referred as the scene puntainer directly


    val centre: Vector
        get() {
            return Vector(width*0.5,height*0.5)
        }


    val thisRectangle: Rectangle
        get() {
            return InternalGlobalAccess.virtualRect
        }

    val virtualWidth: Double
        get() {
            return this.thisRectangle.width
        }

    val virtualHeight: Double
        get() {
            return  this.thisRectangle.height
        }

    /*
    fun <T: PunScene> punScene(clazz: KClass<PunScene> , width: Double=virtualWidth, height: Double=virtualHeight, visible: Boolean = true): PunScene {


        return T(width,height)
        //TODO a direct injector to stage
    }

     */



    /*
    fun getPuntainers(): List<Puntainer> {
        return (sceneView as Puntainer).puntainers.toList()
    }


    fun addPuntainer(p: Puntainer, rectangle: Rectangle=thisRectangle, relative: Boolean=false){
        (sceneView as Puntainer).addPuntainer(p,rectangle,relative)
    }

     */
}