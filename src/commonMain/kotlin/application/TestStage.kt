package application

import com.soywiz.korge.view.*
import com.soywiz.korim.bitmap.Bitmap
import com.soywiz.korim.format.readBitmap
import com.soywiz.korio.async.launchImmediately
import com.soywiz.korio.file.std.resourcesVfs
import modules.basic.Colour
import pungine.*
import pungine.uiElements.PunImage
import kotlin.time.ExperimentalTime
import kotlin.time.measureTime

/** This scene is the template for a PunGineIV game
 *
 */

class TestStage: PunStage() {
    var testScene = TestScene()

    @OptIn(ExperimentalTime::class)
    override suspend fun Container.sceneMain(): Unit{
        testScene.initialize()

        addChild(testScene.scenePuntainer)

        addUpdater { dt->
            testScene.update(dt.seconds)
        }
    }

}