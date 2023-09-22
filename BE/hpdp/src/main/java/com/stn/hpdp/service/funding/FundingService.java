package com.stn.hpdp.service.funding;

import com.stn.hpdp.common.AwsS3Uploader;
import com.stn.hpdp.common.enums.FundingState;
import com.stn.hpdp.common.exception.CustomException;
import com.stn.hpdp.common.util.SecurityUtil;
import com.stn.hpdp.controller.funding.request.ReportFundingReq;
import com.stn.hpdp.controller.funding.request.SaveFundingReq;
import com.stn.hpdp.controller.funding.request.SettleFundingReq;
import com.stn.hpdp.controller.funding.request.UpdateFundingReq;
import com.stn.hpdp.controller.funding.response.SettleFundingRes;
import com.stn.hpdp.dto.FundingInfoForContractDTO;
import com.stn.hpdp.model.entity.Company;
import com.stn.hpdp.model.entity.Funding;
import com.stn.hpdp.model.repository.BudgetRepository;
import com.stn.hpdp.model.repository.CompanyRepository;
import com.stn.hpdp.model.repository.FundingRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Optional;

import static com.stn.hpdp.common.exception.ErrorCode.*;

@Slf4j
@RequiredArgsConstructor
@Service
@Transactional
public class FundingService {

    private final FundingRepository fundingRepository;
    private final BudgetRepository budgetRepository;

    private final CompanyRepository companyRepository;

    private final AwsS3Uploader awsS3Uploader;

    public FundingInfoForContractDTO saveFunding(SaveFundingReq saveFundingReq) {
        Optional<Company> company = companyRepository.findByLoginId(saveFundingReq.getCompanyLoginId());
        if (company.isEmpty()) {
            throw new CustomException(COMPANY_NOT_FOUND);
        }

        Funding funding = saveFundingReq.toEntity(company.get());

        // startdate 따져서 state 세팅
        LocalDateTime startDate = LocalDateTime.parse(saveFundingReq.getStartDate());
        if (startDate.isAfter(LocalDateTime.now())) {
            funding.setState(FundingState.READY);
        } else {
            funding.setState(FundingState.ING);
        }

        // 썸네일 이미지
        if (saveFundingReq.getThumbnail() != null) {
            try {
                String thumbnailUrl = awsS3Uploader.uploadFile(saveFundingReq.getThumbnail(), "funding/thumbnail");
                funding.setThumbnailUrl(thumbnailUrl);
            } catch (IOException e) {
                log.info(e.getMessage());
            }
        }

        // 내용 이미지
        if (saveFundingReq.getContent() != null) {
            try {
                String contentUrl = awsS3Uploader.uploadFile(saveFundingReq.getContent(), "funding/content");
                funding.setContentUrl(contentUrl);
            } catch (IOException e) {
                log.info(e.getMessage());
            }
        }

        // 리워드 이미지
        if (saveFundingReq.getRewardImg() != null) {
            try {
                String rewardUrl = awsS3Uploader.uploadFile(saveFundingReq.getRewardImg(), "funding/reward");
                funding.setRewardImg(rewardUrl);
            } catch (IOException e) {
                log.info(e.getMessage());
            }
        }

        saveFundingReq.getBudgetList().forEach(
                funding::addBudgets
        );

        fundingRepository.save(funding);

        return FundingInfoForContractDTO.builder()
                .companyId(company.get().getId())
                .fundingId(funding.getId())
                .goal((long) funding.getTargetAmount())
                .days(funding.getEndDate())
                .build();
    }

    public void updateFunding(UpdateFundingReq updateFundingReq) {
        Optional<Funding> funding = fundingRepository.findById(Long.parseLong(updateFundingReq.getFundingId()));
        if (funding.isEmpty()) {
            throw new CustomException(FUNDING_NOT_FOUND);
        }

        budgetRepository.deleteAllByFunding_Id(funding.get().getId());
        funding.get().update(updateFundingReq);

        // startdate 따져서 state 세팅
        LocalDateTime startDate = LocalDateTime.parse(updateFundingReq.getStartDate());
        if (startDate.isAfter(LocalDateTime.now())) {
            funding.get().setState(FundingState.READY);
        } else {
            funding.get().setState(FundingState.ING);
        }

        // 썸네일 이미지
        if (updateFundingReq.getThumbnail() != null) {
            try {
                String thumbnailUrl = awsS3Uploader.uploadFile(updateFundingReq.getThumbnail(), "funding/thumbnail");
                funding.get().setThumbnailUrl(thumbnailUrl);
            } catch (IOException e) {
                log.info(e.getMessage());
            }
        }

        // 내용 이미지
        if (updateFundingReq.getContent() != null) {
            try {
                String contentUrl = awsS3Uploader.uploadFile(updateFundingReq.getContent(), "funding/content");
                funding.get().setContentUrl(contentUrl);
            } catch (IOException e) {
                log.info(e.getMessage());
            }
        }

        // 리워드 이미지
        if (updateFundingReq.getRewardImg() != null) {
            try {
                String rewardUrl = awsS3Uploader.uploadFile(updateFundingReq.getRewardImg(), "funding/reward");
                funding.get().setRewardImg(rewardUrl);
            } catch (IOException e) {
                log.info(e.getMessage());
            }
        }

        updateFundingReq.getBudgetList().forEach(
                funding.get()::addBudgets
        );

        fundingRepository.save(funding.get());
    }

    public void deleteFunding(Long fundingId) {
        fundingRepository.deleteById(fundingId);
    }

    public SettleFundingRes settleFunding(SettleFundingReq settleFundingReq) {
        Optional<Funding> funding = fundingRepository.findById(settleFundingReq.getFundingId());
        if (funding.isEmpty()) {
            throw new CustomException(FUNDING_NOT_FOUND);
        }

        String loginId = SecurityUtil.getCurrentMemberLoginId();
        String companyLoginId = funding.get().getCompany().getLoginId();
        if (!loginId.equals(companyLoginId)) {
            throw new CustomException(NOT_COMPANY_FORBIDDEN);
        }

        if (funding.get().getState().equals(FundingState.SETTLE)) {
            throw new CustomException(SETTLE_ALREADY_CONFLICT);
        }

        SettleFundingRes settleFundingRes = SettleFundingRes.of(funding.get());

        // TODO: 후원하기 기능 완료 후 totalPoint 세팅

        // TODO: totalPoint를 settlement로 세팅한 후 update
        // state update
        funding.get().setState(FundingState.SETTLE);
        fundingRepository.save(funding.get());

        return settleFundingRes;
    }


    public void reportFunding(ReportFundingReq reportFundingReq) {
        Optional<Funding> funding = fundingRepository.findById(Long.parseLong(reportFundingReq.getFundingId()));
        if (funding.isEmpty()) {
            throw new CustomException(FUNDING_NOT_FOUND);
        }

        String loginId = SecurityUtil.getCurrentMemberLoginId();
        String companyLoginId = funding.get().getCompany().getLoginId();
        if (!loginId.equals(companyLoginId)) {
            throw new CustomException(NOT_COMPANY_FORBIDDEN);
        }

        // 보고서 파일
        if (reportFundingReq.getDocs() != null) {
            try {
                String docsUrl = awsS3Uploader.uploadFile(reportFundingReq.getDocs(), "funding/docs");
                funding.get().setDocsUrl(docsUrl);
            } catch (IOException e) {
                log.info(e.getMessage());
            }
        }

        fundingRepository.save(funding.get());
    }

}