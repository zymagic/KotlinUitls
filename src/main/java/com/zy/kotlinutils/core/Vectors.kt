package com.zy.kotlinutils.core

data class Vector2(var x: Float, var y: Float)
data class Vector3(var x: Float, var y: Float, var z: Float)
data class Vector4(var x: Float, var y: Float, var z: Float, var w: Float)

/*=== operators for vector2 ====*/

operator fun Vector2.plus(other: Vector2) = Vector2(x + other.x, y + other.y)
operator fun Vector2.minus(other: Vector2) = Vector2(x - other.x, y - other.y)
operator fun Vector2.times(other: Vector2) = Vector2(x * other.x, y * other.y)
operator fun Vector2.times(num: Float) = Vector2(x * num, y * num)
operator fun Float.times(v: Vector2) = Vector2(this * v.x, this * v.y)
operator fun Vector2.div(num: Float) = Vector2(x / num, y / num)
operator fun Vector2.div(other: Vector2) = Vector2(x / other.x, y / other.y)
operator fun Float.div(v: Vector2) = Vector2(this / v.x, this / v.y)
operator fun Vector2.timesAssign(num: Float) {
    x *= num
    y *= num
}
operator fun Vector2.timesAssign(other: Vector2) {
    x *= other.x
    y *= other.y
}
operator fun Vector2.plusAssign(other: Vector2) {
    x += other.x
    y += other.y
}
operator fun Vector2.minusAssign(other: Vector2) {
    x -= other.x
    y -= other.y
}
operator fun Vector2.divAssign(num: Float) {
    x /= num
    y /= num
}
operator fun Vector2.divAssign(other: Vector2) {
    x /= other.x
    y /= other.y
}
operator fun Vector2.unaryMinus() = Vector2(-x, -y)

/*=== operators for vector3 ====*/

operator fun Vector3.plus(other: Vector3) = Vector3(x + other.x, y + other.y, z + other.z)
operator fun Vector3.minus(other: Vector3) = Vector3(x - other.x, y - other.y, z - other.z)
operator fun Vector3.times(other: Vector3) = Vector3(x * other.x, y * other.y, z * other.z)
operator fun Vector3.times(num: Float) = Vector3(x * num, y * num, z * num)
operator fun Float.times(v: Vector3) = Vector3(this * v.x, this * v.y, this * v.z)
operator fun Vector3.div(num: Float) = Vector3(x / num, y / num, z / num)
operator fun Vector3.div(other: Vector3) = Vector3(x / other.x, y / other.y, z / other.z)
operator fun Float.div(v: Vector3) = Vector3(this / v.x, this / v.y, this / v.z)
operator fun Vector3.timesAssign(num: Float) {
    x *= num
    y *= num
    z *= num
}
operator fun Vector3.timesAssign(other: Vector3) {
    x *= other.x
    y *= other.y
    z *= other.z
}
operator fun Vector3.plusAssign(other: Vector3) {
    x += other.x
    y += other.y
    z += other.z
}
operator fun Vector3.minusAssign(other: Vector3) {
    x -= other.x
    y -= other.y
    z -= other.z
}
operator fun Vector3.divAssign(num: Float) {
    x /= num
    y /= num
    z /= num
}
operator fun Vector3.divAssign(other: Vector3) {
    x /= other.x
    y /= other.y
    z /= other.z
}
operator fun Vector3.unaryMinus() = Vector3(-x, -y, -z)

/*=== operators for vector4 ====*/

operator fun Vector4.plus(other: Vector4) = Vector4(x + other.x, y + other.y, z + other.z, w + other.w)
operator fun Vector4.minus(other: Vector4) = Vector4(x - other.x, y - other.y, z - other.z, w - other.w)
operator fun Vector4.times(other: Vector4) = Vector4(x * other.x, y * other.y, z * other.z, w * other.w)
operator fun Vector4.times(num: Float) = Vector4(x * num, y * num, z * num, w * num)
operator fun Float.times(v: Vector4) = Vector4(this * v.x, this * v.y, this * v.z, this * v.w)
operator fun Vector4.div(num: Float) = Vector4(x / num, y / num, z / num, w / num)
operator fun Vector4.div(other: Vector4) = Vector4(x / other.x, y / other.y, z / other.z, w / other.w)
operator fun Float.div(v: Vector4) = Vector4(this / v.x, this / v.y, this / v.z, this / v.w)
operator fun Vector4.timesAssign(num: Float) {
    x *= num
    y *= num
    z *= num
    w *= num
}
operator fun Vector4.timesAssign(other: Vector4) {
    x *= other.x
    y *= other.y
    z *= other.z
    w *= other.w
}
operator fun Vector4.plusAssign(other: Vector4) {
    x += other.x
    y += other.y
    z += other.z
    w += other.w
}
operator fun Vector4.minusAssign(other: Vector4) {
    x -= other.x
    y -= other.y
    z -= other.z
    w -= other.w
}
operator fun Vector4.divAssign(num: Float) {
    x /= num
    y /= num
    z /= num
    z /= num
}
operator fun Vector4.divAssign(other: Vector4) {
    x /= other.x
    y /= other.y
    z /= other.z
    w /= other.w
}
operator fun Vector4.unaryMinus() = Vector4(-x, -y, -z, -w)

/*===== cross operators ======*/

operator fun Vector3.plus(v: Vector2) = Vector3(x + v.x, y + v.y, z)
operator fun Vector2.plus(v: Vector3) = Vector3(x + v.x, y + v.y, v.z)
operator fun Vector4.plus(v: Vector2) = Vector4(x + v.x, y + v.y, z, w)
operator fun Vector2.plus(v: Vector4) = Vector4(x + v.x, y + v.y, v.z, v.w)
operator fun Vector4.plus(v: Vector3) = Vector4(x + v.x, y + v.y, z + v.z, w)
operator fun Vector3.plus(v: Vector4) = Vector4(x + v.x, y + v.y, z + v.z, v.w)
operator fun Vector3.plusAssign(v: Vector2) {
    x += v.x
    y += v.y
}
operator fun Vector4.plusAssign(v: Vector2) {
    x += v.x
    y += v.y
}
operator fun Vector4.plusAssign(v: Vector3) {
    x += v.x
    y += v.y
    z += v.z
}

operator fun Vector3.minus(v: Vector2) = Vector3(x - v.x, y - v.y, z)
operator fun Vector4.minus(v: Vector2) = Vector4(x - v.x, y - v.y, z, w)
operator fun Vector4.minus(v: Vector3) = Vector4(x - v.x, y - v.y, z - v.z, w)
operator fun Vector3.minusAssign(v: Vector2) {
    x -= v.x
    y -= v.y
}
operator fun Vector4.minusAssign(v: Vector2) {
    x -= v.x
    y -= v.y
}
operator fun Vector4.minusAssign(v: Vector3) {
    x -= v.x
    y -= v.y
    z -= v.z
}

operator fun Vector3.times(v: Vector2) = Vector3(x * v.x, y * v.y, z)
operator fun Vector2.times(v: Vector3) = Vector3(x * v.x, y * v.y, v.z)
operator fun Vector4.times(v: Vector2) = Vector4(x * v.x, y * v.y, z, w)
operator fun Vector2.times(v: Vector4) = Vector4(x * v.x, y * v.y, v.z, v.w)
operator fun Vector4.times(v: Vector3) = Vector4(x * v.x, y * v.y, z * v.z, w)
operator fun Vector3.times(v: Vector4) = Vector4(x * v.x, y * v.y, v.z, v.w)
operator fun Vector3.timesAssign(v: Vector2) {
    x *= v.x
    y *= v.y
}
operator fun Vector4.timesAssign(v: Vector2) {
    x *= v.x
    y *= v.y
}
operator fun Vector4.timesAssign(v: Vector3) {
    x *= v.x
    y *= v.y
    z *= v.z
}

operator fun Vector3.div(v: Vector2) = Vector3(x / v.x, y / v.y, z)
operator fun Vector4.div(v: Vector2) = Vector4(x / v.x, y / v.y, z, w)
operator fun Vector4.div(v: Vector3) = Vector4(x / v.x, y / v.y, z / v.z, w)
operator fun Vector3.divAssign(v: Vector2) {
    x /= v.x
    y /= v.y
}
operator fun Vector4.divAssign(v: Vector2) {
    x /= v.x
    y /= v.y
}
operator fun Vector4.divAssign(v: Vector3) {
    x /= v.x
    y /= v.y
    z /= v.z
}

infix fun Vector2.dot(other: Vector2) = x * other.x + y * other.y
infix fun Vector3.dot(other: Vector3) = x * other.x + y * other.y + z * other.z
infix fun Vector4.dot(other: Vector4) = x * other.x + y * other.y + z * other.z + w * other.w

fun Vector2.length() = hypot(x, y)
fun Vector3.length() = hypot(x, y, z)
fun Vector4.length() = hypot(x, y, z, w)

infix fun Vector2.distance(other: Vector2) = hypot(x - other.x, y - other.y)
infix fun Vector3.distance(other: Vector3) = hypot(x - other.x, y - other.y, z - other.z)
infix fun Vector4.distance(other: Vector4) = hypot(x - other.x, y - other.y, z - other.z, w - other.w)

fun Vector2.normalize() : Vector2 {
    val len = this.length()
    if (len > 0) {
        x /= len
        y / len
    }
    return this
}

fun Vector3.normalize() : Vector3 {
    val len = this.length()
    if (len > 0) {
        x /= len
        y /= len
        z /= len
    }
    return this
}

fun Vector4.normalize() : Vector4 {
    val len = this.length()
    if (len > 0) {
        x /= len
        y /= len
        z /= len
        w /= len
    }
    return this
}

fun Vector4.rgb() = Vector3(y, z, w)

infix fun Float.with(a : Vector2) = Vector3(this, a.x, a.y)
infix fun Vector2.with(a: Float) = Vector3(x, y, a)
infix fun Vector2.with(a: Vector2) = Vector4(x, y, a.x, a.y)
infix fun Float.with(a: Vector3) = Vector4(this, a.x, a.y, a.z)
infix fun Vector3.with(a: Float) = Vector4(x, y, z, a)



fun testVector() {

}