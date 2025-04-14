package funn.j2k.politicsMc.custom_map

import funn.j2k.politicsMc.utilities.Grid
import org.bukkit.Color
import org.joml.Vector4d

interface ShaderProgram {
    val vertex: VertexShader
    val fragment: FragmentShader
}

class VertexData(val position: Vector4d, val pixel: DoubleArray)

class FragmentData(var color: Color, var depth: Double = Double.NaN)

interface Mesh {
    val vertices: Array<DoubleArray>
    val indices: IntArray
}

typealias VertexShader = (vertex: DoubleArray) -> VertexData

typealias FragmentShader = (pixel: DoubleArray) -> FragmentData

typealias RenderBuffer = Grid<FragmentData>
