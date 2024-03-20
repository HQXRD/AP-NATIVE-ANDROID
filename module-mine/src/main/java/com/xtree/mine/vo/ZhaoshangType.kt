import kotlin.math.roundToInt

enum class RebateUserType(val value: Int) {
    UNKNOWN(0),
    ROOT_MANAGER(1),
    DAZHAOSHANG(2),
    ZHAOSHANG(3),
    MEMBER(4);

    val descriptionCN: String
        get() = when (this) {
            UNKNOWN -> "未知"
            ROOT_MANAGER -> "管理员"
            DAZHAOSHANG -> "大招商"
            ZHAOSHANG -> "招商"
            MEMBER -> "会员"
        }

    companion object {
        fun getRebaseType(typeName: String): Int {
            return when (typeName) {
                "管理员" -> ROOT_MANAGER.value
                "大招商" -> DAZHAOSHANG.value
                "招商" -> ZHAOSHANG.value
                "会员" -> MEMBER.value
                else -> UNKNOWN.value
            }
        }
    }
}

enum class RebateType(val descriptionEN: String, val descriptionCN: String) {
    NOT_POINT("notPoint", "非返点"),
    LOTTERY_POINT("lotteryPoint", "彩票返点"),
    LIVE_POINT("livePoint", "真人返点"),
    SPORT_POINT("sportPoint", "体育返点"),
    CHESS_POINT("chessPoint", "棋牌返点"),
    ESPORTS_POINT("esportsPoint", "电竞返点")
}

interface Convertible

class PromoRegTextModel(
    var title: String = "",
    var placeholder: String = "",
    var descriptioncn: String = "",
    var cellType: Int = 1,
    var status: Int = 0,
    var maxRebate: Double = 0.0,
    var subRebate: Double = 0.0,
    var selfRebate: Double = 0.0,
    var rebateType: RebateType = RebateType.NOT_POINT
) : Convertible

class PromoRebateModel(
    var maxRebate: Double = 0.0,
    var subRebate: Double = 0.0,
    var selfRebate: Double = 0.0,
    var status: Boolean = false,
    var rebateType: RebateType = RebateType.NOT_POINT
) : Convertible

enum class PromoVCType {
    // define your enum values here
}

class Link {
    // define your Link class here
}

object PromoRegModel : Convertible {
    var title: String = "注册推广"
    var titles: List<String> = listOf("注册开户", "推广链接")
    var regaccountTitles: List<String> = listOf("开户配额", "快速返点设置")
    var promoLinkTitles: List<String> = listOf("下级返点", "您的推广链接")
    var userType: Int = 1
    var rebateUserType: RebateUserType = RebateUserType.DAZHAOSHANG
    var typeTitles: List<String> = emptyList()
    var lotteryPoint: String = "4"
    var livePoint: String = "1"
    var sportPoint: String = "1"
    var chessPoint: String = "1"
    var esportsPoint: String = "1"
    var userName: String = ""
    var userPassword: String = ""
    var userNickName: String = ""
    var sectionTitles: List<List<String>> = listOf(listOf("开户配额", "快速返点设置"), listOf("下级返点", "您的推广链接"))
    var promoLinkUrls: List<Link> = emptyList()
    var promoLinkDomains: List<String> = emptyList()
    //注册开户
    var regAccountTitle: List<List<PromoRegTextModel>> = listOf(
        listOf(
            PromoRegTextModel("类型", "大招商", "", 1, 1),
            PromoRegTextModel("用户名", "用户名", "字母或数字6-16个字符，不能连续四位相同，首字不能0或者o", 2, 1),
            PromoRegTextModel("登录密码", "登录密码", "由字母和数字组成的6-16个字符，且必须包含字符和数字", 2, 1),
            PromoRegTextModel("用户昵称", "用户昵称", "由2-6个字符组成", 3, 1)
        ),
        listOf(
            PromoRegTextModel("彩票返点", "4.0%", "自身保留返点：0%", 4, 0, 4.0, 4.0, 0.0, RebateType.LOTTERY_POINT),
            PromoRegTextModel("真人返点", "1.0%", "自身保留返点：0%", 4, 0, 1.0, 1.0, 0.0, RebateType.LIVE_POINT),
            PromoRegTextModel("体育返点", "1.0%", "自身保留返点：0%", 4, 0, 1.0, 1.0, 0.0, RebateType.SPORT_POINT),
            PromoRegTextModel("棋牌返点", "1.0%", "自身保留返点：0%", 4, 0, 1.0, 1.0, 0.0, RebateType.CHESS_POINT),
            PromoRegTextModel("电竞返点", "1.0%", "自身保留返点：0%", 4, 0, 1.0, 1.0, 0.0, RebateType.ESPORTS_POINT)
        )
    )
    //推广链接
    var promoLinkTitle: List<List<PromoRegTextModel>> = listOf(
        listOf(
            PromoRegTextModel("类型", "大招商", "", 1, 1),
            PromoRegTextModel("彩票返点", "4.0%", "自身保留返点：0%", 4, 0, 4.0, 4.0, 0.0, RebateType.LOTTERY_POINT),
            PromoRegTextModel("真人返点", "0.9%", "自身保留返点：0%", 4, 1, 1.0, 0.9, 0.1, RebateType.LIVE_POINT),
            PromoRegTextModel("体育返点", "0.9%", "自身保留返点：0%", 4, 1, 1.0, 0.9, 0.1, RebateType.SPORT_POINT),
            PromoRegTextModel("棋牌返点", "0.9%", "自身保留返点：0%", 4, 1, 1.0, 0.9, 0.1, RebateType.CHESS_POINT),
            PromoRegTextModel("电竞返点", "0.9%", "自身保留返点：0%", 4, 1, 1.0, 0.9, 0.1, RebateType.ESPORTS_POINT)
        ),
        listOf(
            PromoRegTextModel(
                "以下为您的推广链接\n您可以在下拉框中选择链接，点击复制进行推广",
                "彩票游戏-链接：",
                "温馨提示：点击“保存更新”后，您所有推广链接的相关设定将全部更新,请谨慎操作。",
                5,
                1
            )
        )
    )
    //快速返点
    var typeRebate: List<List<PromoRebateModel>> = listOf(
        listOf(
            PromoRebateModel(4.0, 4.0, 0.0, false, RebateType.LOTTERY_POINT),
            PromoRebateModel(1.0, 1.0, 0.0, false, RebateType.LIVE_POINT),
            PromoRebateModel(1.0, 1.0, 0.0, false, RebateType.SPORT_POINT),
            PromoRebateModel(1.0, 1.0, 0.0, false, RebateType.CHESS_POINT),
            PromoRebateModel(1.0, 1.0, 0.0, false, RebateType.ESPORTS_POINT)
        ),
        listOf(
            PromoRebateModel(4.0, 4.0, 0.0, false, RebateType.LOTTERY_POINT),
            PromoRebateModel(1.0, 0.9, 0.1, true, RebateType.LIVE_POINT),
            PromoRebateModel(1.0, 0.9, 0.1, true, RebateType.SPORT_POINT),
            PromoRebateModel(1.0, 0.9, 0.1, true, RebateType.CHESS_POINT),
            PromoRebateModel(1.0, 0.9, 0.1, true, RebateType.ESPORTS_POINT)
        ),
        listOf(
            PromoRebateModel(4.0, 4.0, 0.0, true, RebateType.LOTTERY_POINT),
            PromoRebateModel(1.0, 0.5, 0.5, false, RebateType.LIVE_POINT),
            PromoRebateModel(1.0, 0.5, 0.5, false, RebateType.SPORT_POINT),
            PromoRebateModel(1.0, 0.5, 0.5, false, RebateType.CHESS_POINT),
            PromoRebateModel(1.0, 0.5, 0.5, false, RebateType.ESPORTS_POINT)
        )
    )
    //下级
    var subRebate: List<List<PromoRebateModel>> = listOf(
        listOf(
            PromoRebateModel(4.0, 4.0, 0.0, false, RebateType.LOTTERY_POINT),
            PromoRebateModel(1.0, 1.0, 0.0, false, RebateType.LIVE_POINT),
            PromoRebateModel(1.0, 1.0, 0.0, false, RebateType.SPORT_POINT),
            PromoRebateModel(1.0, 1.0, 0.0, false, RebateType.CHESS_POINT),
            PromoRebateModel(1.0, 1.0, 0.0, false, RebateType.ESPORTS_POINT)
        ),
        listOf(
            PromoRebateModel(4.0, 4.0, 0.0, false, RebateType.LOTTERY_POINT),
            PromoRebateModel(1.0, 0.9, 0.1, true, RebateType.LIVE_POINT),
            PromoRebateModel(1.0, 0.9, 0.1, true, RebateType.SPORT_POINT),
            PromoRebateModel(1.0, 0.9, 0.1, true, RebateType.CHESS_POINT),
            PromoRebateModel(1.0, 0.9, 0.1, true, RebateType.ESPORTS_POINT)
        ),
        listOf(
            PromoRebateModel(4.0, 4.0, 0.0, true, RebateType.LOTTERY_POINT),
            PromoRebateModel(1.0, 0.5, 0.5, false, RebateType.LIVE_POINT),
            PromoRebateModel(1.0, 0.5, 0.5, false, RebateType.SPORT_POINT),
            PromoRebateModel(1.0, 0.5, 0.5, false, RebateType.CHESS_POINT),
            PromoRebateModel(1.0, 0.5, 0.5, false, RebateType.ESPORTS_POINT)
        )
    )
}