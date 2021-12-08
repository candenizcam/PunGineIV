package pungine

import com.soywiz.korge.input.mouse
import com.soywiz.korge.view.RoundRect
import com.soywiz.korge.view.solidRect
import com.soywiz.korim.bitmap.Bitmap
import com.soywiz.korim.bitmap.BitmapSlice
import com.soywiz.korinject.AsyncInjectorContext
import com.soywiz.korio.async.async
import com.soywiz.korio.async.launchImmediately
import modules.basic.Colour
import pungine.geometry2D.Rectangle
import pungine.geometry2D.Vector
import pungine.geometry2D.oneRectangle
import pungine.uiElements.PunImage
import pungine.uiElements.PunText

/** This is the class to inherit when designing a game
 * it is liberated from the clunky view and only has given methods
 * if you dont know about the backend of the frontend, you should not go any deeper
 */
open class PunScene(rectangle: Rectangle, bgColour: Colour=Colour.WHITE){
    var sceneRect: Rectangle
    val puntainers = mutableListOf<Puntainer>()
    val bgColour=bgColour
    lateinit var scenePuntainer: Puntainer
        protected set
    init {
        sceneRect = rectangle

    }

    constructor(width: Double,height: Double, bgColour: Colour=Colour.WHITE) : this(Rectangle(0.0,width,0.0,height),bgColour)

    open suspend fun sceneBeforeInit(){
        scenePuntainer = Puntainer("scenePuntainer",sceneRect)
        setBg(sceneRect.width,sceneRect.height,bgColour)
    }

    open suspend fun sceneInit(){

    }

    open suspend fun sceneAfterInit(){

    }

    suspend fun initialize(){
        sceneBeforeInit()
        sceneInit()
        sceneAfterInit()
    }

    open fun update(ms: Double){

    }

    fun relativeMousePoint(id: String): Vector {
        return relativePuntainerPoint(id, mousePositionPixel)
    }

    fun relativePuntainerPoint(id: String, scenePoint: Vector): Vector {
        return puntainers.first { it.id==id }.relativePoint(sceneRect.ratePoint(scenePoint))
    }

    /** This is even safer
     *
     */
    fun toPuntainer(id: String,onlyFirst: Boolean=false, func: (Puntainer)->Unit){
        puntainers.filter { it.id==id }.also{
            if(onlyFirst){
                toPuntainer(it.first(),func)
            }else{
                it.forEach {
                    toPuntainer(it,func)
                }
            }
        }

    }

    /** This function is called from body (possibly updater) and is used to influence puntainers in a safe way
     *
     */
    fun toPuntainer(puntainer: Puntainer,func: (Puntainer)->Unit){
        val initialRect = puntainer.relativeRectangle
        val initialZ = puntainer.zOrder
        val initialText = if(puntainer is PunText){ puntainer.text }else{ "" }
        func(puntainer)

        if(puntainer.relativeRectangle!=initialRect){
            puntainer.reshape(sceneRect.fromRated(puntainer.relativeRectangle))
        }
        if(puntainer.zOrder!=initialZ){
            scenePuntainer.sortPuntainersByZ()
        }
        if (if(puntainer is PunText){ puntainer.text }else{ "" }==initialText){
            puntainer.reshape(sceneRect.fromRated(puntainer.relativeRectangle))
        }
    }



    val mousePositionPixel: Vector
    get() {
        scenePuntainer.mouse {
            return Vector(this.currentPosStage.x,sceneRect.height - this.currentPosStage.y)
        }
    }

    val mousePositionRated: Vector
        get() {
            return sceneRect.ratePoint(mousePositionPixel)
        }

    val virtualWidth: Double
        get() {
            return sceneRect.width
        }
    val virtualHeight: Double
        get() {
            return sceneRect.height
        }


    fun updateChildrenShapes(){
        puntainers.forEach {
            it.reshape(sceneRect.fromRated(it.relativeRectangle))
        }
    }


    /** This sets bg to a given colour
     * this is to set a size for the puntainer so that it does not try to get playful on its own
     */
    private fun setBg(width: Double, height: Double, colour: Colour =Colour.WHITE): Puntainer {
        return Puntainer("bg").also {
            it.solidRect(width,height,colour.korgeColor)
            addPuntainer(it)
        }
    }

    /** This is used to add a puntainer to the scene
     * ideally this is private and the ones below are accessed
     * but if user wants to make their own Puntainer, this is the function to put it on the screen
     */
    fun addPuntainer(puntainer: Puntainer){
        scenePuntainer.addChild(puntainer)
        addToPuntainers(puntainer)
    }

    private fun addToPuntainers(puntainer: Puntainer){
        puntainers.add(puntainer)
        puntainer.reshape(sceneRect.fromRated(puntainer.relativeRectangle))
        scenePuntainer.sortPuntainersByZ()

    }

    fun solidRect(id: String?=null, rectangle: Rectangle= oneRectangle(), colour: Colour=Colour.WHITE, zOrder: Int=0): Puntainer {
        return scenePuntainer.solidRect(id, rectangle, colour,zOrder).also {
            addToPuntainers(it)
        }
    }

    fun roundRect(id: String?=null, relativeRectangle: Rectangle= oneRectangle(), rX: Double, rY: Double, colour: Colour =Colour.WHITE, zOrder: Int=0): Puntainer{

        return scenePuntainer.roundRect(id,relativeRectangle,rX,rY,colour,zOrder).also{
            addToPuntainers(it)
        }
    }

    fun punImage(id: String?=null, relativeRectangle: Rectangle= oneRectangle(), bitmap: Bitmap, zOrder: Int=0, editable: Boolean=false): Puntainer{
        return scenePuntainer.punImage(id,relativeRectangle,bitmap,zOrder,editable).also {
            addPuntainer(it)
        }

    }

    fun punImage(id: String?=null, relativeRectangle: Rectangle= oneRectangle(), bitmap: BitmapSlice<Bitmap>, zOrder: Int=0, editable: Boolean=false): Puntainer{
        return scenePuntainer.punImage(id,relativeRectangle,bitmap,zOrder,editable).also {
            addPuntainer(it)
        }

    }


}