package pungine.uiElements

import com.soywiz.korge.view.Image
import com.soywiz.korim.bitmap.Bitmap
import com.soywiz.korim.bitmap.slice
import com.soywiz.korim.color.RGBA
import modules.basic.Colour
import pungine.geometry2D.Rectangle
import pungine.geometry2D.oneRectangle

/** This class handles simple image and it takes bitmap as an input,
 * It does not inherit Image directly, but it generates an Image into the puntainer, which it Inherits
 *
 */
class PunImageEditable: PunImage {
    constructor(id: String?=null, relativeRectangle: Rectangle = oneRectangle(), bitmap: Bitmap, zOrder: Int=0): super(id,relativeRectangle,bitmap,zOrder){
        bitmapDisplayed = bitmap
        bitmapSource = bitmap.clone()
    }




    var bitmapSource: Bitmap // this is the original form of the bitmap
        private set


    var bitmapDisplayed: Bitmap // this is the altered form of the bitmap
        private set







    val widthIndex: IntRange
        get() {
            return (0 until this.bitmapDisplayed.width)
        }

    val heightIndex: IntRange
        get() {
            return (0 until this.bitmapDisplayed.height)
        }





    /** This is the kind of function that i'd hug & never let go
     * it takes a function as an input which takes two ints, which are i and j indices of pixels and runs the function for each pixel
     */
    fun applyToPixels(f: (Int, Int)->Any){
        for(i in widthIndex){
            for(j in heightIndex){
                f(i,j)
            }
        }
    }




    fun setBitmap(bitmap: Bitmap){
        bitmapSource = bitmap
        this.bitmapDisplayed = bitmap
    }

    fun resetBitmap(){
        this.bitmapDisplayed = bitmapSource.clone()
        applyModifications()
    }

    /** Upon modifying image, this function is called to change the displayed image
     */
    fun applyModifications(){
        (this.children[0] as Image).bitmap = this.bitmapDisplayed.clone().slice()
    }


    fun getPixel(x: Int, y: Int): Colour {
        return Colour.byKorgeRGBA(bitmapDisplayed.getRgbaClamped(x,y))
    }

    fun setPixel(x:Int,y:Int,rgba: RGBA){
        bitmapDisplayed.setRgba(x,y,rgba)

    }

    fun setPixel(x:Int, y: Int, colour: Colour){
        bitmapDisplayed.setRgba(x,y,colour.korgeColor)
    }




}
