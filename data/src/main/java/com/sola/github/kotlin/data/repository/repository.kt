package com.sola.github.kotlin.data.repository

import com.sola.github.kotlin.domain.dtos.ClubDetailInfoDTO
import com.sola.github.kotlin.domain.dtos.ClubInfoDTO
import com.sola.github.kotlin.domain.dtos.UserInfoDTO
import com.sola.github.kotlin.domain.repository.ClubCenterRepository
import com.sola.github.kotlin.domain.repository.UserCenterRepository
import io.reactivex.Maybe
import io.reactivex.Observable
import io.reactivex.Single
import java.util.*
import javax.inject.Inject

/**
 * Created by Sola
 * 2017/6/1.
 */
class UserCenterDataRepository @Inject constructor() : UserCenterRepository {

    override fun requestUserInfo(userId: String): Maybe<UserInfoDTO> = Maybe.just(UserInfoDTO())

}

class ClubCenterDataRepository @Inject constructor() : ClubCenterRepository {


    val random: Random = Random()

    override fun requestClubList(): Observable<Collection<ClubInfoDTO>> =
            Observable.just(randomClubList())

    override fun requestClubDetail(id: Int): Single<ClubDetailInfoDTO>
            = Single.just(
            ClubDetailInfoDTO(id,
                    "CLUB[$id]", "", random.nextInt(10),
                    "It's description fo club[$id]", "ShangHai Location For company",
                    Pair(random.nextInt(400), random.nextInt(500)),
                    "2017-06-14 10:20:20", random.nextInt(10000)
            ))

    fun randomClubList(): Collection<ClubInfoDTO> {
        val count = random.nextInt(20)
        val retList: MutableCollection<ClubInfoDTO> = mutableListOf()
        (1..count).mapTo(retList) {
            ClubInfoDTO("CLUB[$it]", it, "",
                    Pair(random.nextInt(400),
                            random.nextInt(400)),
                    random.nextInt(10000), it)
        }
        return retList
    }
}