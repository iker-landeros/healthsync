package mx.tec.healthsyncapp.model

data class Component(
    val idComponent: Int,
    val componentName: String
){
    override fun toString(): String {
        return componentName
    }
}
