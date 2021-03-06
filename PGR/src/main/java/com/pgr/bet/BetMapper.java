package com.pgr.bet;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.pgr.model.BetDomain;
import com.pgr.model.BetDomain2;
import com.pgr.model.BetEntity;
import com.pgr.model.RecentEntity;

@Mapper
public interface BetMapper {
	int insBet(BetEntity p);
	List<BetEntity> selBetList(RecentEntity p);
	BetEntity selBetUser(BetEntity p);
	List<BetDomain2> selBetUserPk(BetEntity p);
	int updBetSuccess(BetEntity p);
	int updBetUser(BetEntity p);
	BetDomain selBetAllocation(BetEntity p);
}
