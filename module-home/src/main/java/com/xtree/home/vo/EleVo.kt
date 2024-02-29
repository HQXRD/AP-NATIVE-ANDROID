package com.xtree.home.vo

data class EleVo(
    val count: String,
    val list: List<Ele>
)

data class Ele(
    val added_at: String,
    val cate_id: Int,
    val code: String,
    val id: Int,
    val is_hot: String,
    val name: String,
    val picture: String,
    val platform_id: Int

) {
    override fun toString(): String {
        //Ele(added_at='null', cate_id=0, code='84', id=3230, is_hot='1', name='赏金女王', picture='/images/pg/84.png', platform_id=19)
        return "Ele(added_at='$added_at', cate_id=$cate_id, code='$code', id=$id, is_hot='$is_hot', name='$name', picture='$picture', platform_id=$platform_id)"
    }
}