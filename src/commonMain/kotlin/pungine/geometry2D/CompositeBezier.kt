package pungine.geometry2D

import com.soywiz.klock.measureTime
import com.soywiz.korim.vector.Context2d
import com.soywiz.korma.geom.bezier.Bezier
import com.soywiz.korma.geom.vector.cubic
import pungine.uiElements.PunImage
import kotlin.math.cos
import kotlin.math.sin

/** This class takes bezier from KorIM to handle composite bezier curve construction
 * important terminology: point, a point that curve passes, relative point is relative to that and control is relative + point
 */
class CompositeBezier {
    constructor(firstPoint: Vector, firstRelativePoint: Vector){
        val b = BezierPoint(firstPoint,firstRelativePoint*(-1))
        points.add(b)
    }


    fun addPointAndRelative(point: Vector,relativePoint: Vector){
        points.add(BezierPoint(point,relativePoint))
    }

    fun addPointAndControl(point: Vector, controlPoint: Vector){
        points.add(BezierPoint(point,controlPoint-point))
    }

    fun addPointAndAngle(point: Vector, angle: Double, length: Double){
        points.add(BezierPoint(point,angle, length))
    }

    fun removeLastPoint(){
        points.removeLast()
    }

    fun getCubicBeziers(canvasHeight: Int? = null): MutableList<Bezier.Cubic> {
        val points = if(canvasHeight!=null){
            val top = Vector(0.0,canvasHeight.toDouble())
            points.map { BezierPoint(Vector(it.point.x,canvasHeight.toDouble()-it.point.y),Vector(it.relativePoint.x,-it.relativePoint.y)) }
        }else{
            points
        }

        val curves = mutableListOf<Bezier.Cubic>()
        for(i in 0 until points.size-1){

            curves.add(points[i].getCurve(points[i+1]))
        }
        return curves
    }

    fun getEdgePoints(): List<Vector> {
        return points.map { it.point }
    }

    fun getControlPoints(): List<Vector> {
        val ps = mutableListOf<Vector>()
        for (i in 0 until points.size-1){
            ps.add(points[i].firstControlPoint())
            ps.add(points[i+1].secondControlPoint())
        }
        return ps.toList()
    }

    fun drawLine(context : Context2d){
        getCubicBeziers(context.height).forEach {
            context.cubic(it)
        }
    }

    fun closestPointTo(point: Vector): Vector {
        var minPoint = Vector(0.0,0.0)
        var mint = 1000.0
        getCubicBeziers().forEachIndexed { index, it->
            for (i in 0..100){
                val p = Vector(it.calc(i.toDouble()/100.0))
                (point-p).length.also {l->
                    if(l<mint){
                        mint = l
                        minPoint = p
                    }
                }
            }
        }
        return minPoint
    }






    private val points = mutableListOf<BezierPoint>()




    data class BezierPoint(val point: Vector, val relativePoint: Vector){
        constructor(p: Vector, angle: Double, length: Double): this(p,Vector(cos(angle)*length,sin(angle)*length))

        fun firstControlPoint(): Vector {
            return point - relativePoint
        }

        fun secondControlPoint(): Vector{
            return point + relativePoint
        }

        fun getCurve(other: BezierPoint): Bezier.Cubic {
            return Bezier.Cubic(point.korgePoint,firstControlPoint().korgePoint,other.secondControlPoint().korgePoint,other.point.korgePoint)
        }

    }
}