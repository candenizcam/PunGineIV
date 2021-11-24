package pungine.uiElements

import com.soywiz.korge.view.Image
import com.soywiz.korim.bitmap.Bitmap
import com.soywiz.korim.bitmap.BitmapSlice
import pungine.Puntainer
import pungine.geometry2D.Rectangle
import pungine.geometry2D.oneRectangle

/** This class handles simple image and it takes bitmap as an input,
 * It does not inherit Image directly, but it generates an Image into the puntainer, which it Inherits
 *
 */
open class PunImage: Puntainer {
    constructor(id: String?=null, relativeRectangle: Rectangle= oneRectangle(), bitmap: Bitmap, zOrder: Int=0): super(id,relativeRectangle,zOrder){
        image = Image(bitmap)
        this.addChild(image)
    }

    constructor(id: String?=null, relativeRectangle: Rectangle= oneRectangle(), bitmapSlice: BitmapSlice<Bitmap>, zOrder: Int=0): super(id,relativeRectangle,zOrder){
        image = Image(bitmapSlice)
        this.addChild(image)
    }




    private val image: Image
}