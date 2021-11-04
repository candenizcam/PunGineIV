package pungine

import com.soywiz.korim.bitmap.Bitmap

class PunBitmap(b: Bitmap): Bitmap(b.width,b.height,b.bpp,b.premultiplied,b.backingArray) {
}