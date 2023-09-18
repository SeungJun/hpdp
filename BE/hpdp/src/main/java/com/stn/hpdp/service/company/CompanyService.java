package com.stn.hpdp.service.company;

import com.stn.hpdp.common.exception.CustomException;
import com.stn.hpdp.common.jwt.JwtTokenProvider;
import com.stn.hpdp.controller.company.response.FindCompanyDetailRes;
import com.stn.hpdp.controller.company.response.FindCompanyRes;
import com.stn.hpdp.model.entity.Company;
import com.stn.hpdp.model.repository.CompanyQueryRepository;
import com.stn.hpdp.model.repository.CompanyRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.swing.text.html.Option;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.stn.hpdp.common.exception.ErrorCode.ACCESS_TOKEN_INVALID;
import static com.stn.hpdp.common.exception.ErrorCode.COMPANY_NOT_FOUND;

@Slf4j
@RequiredArgsConstructor
@Service
public class CompanyService {

    private final CompanyRepository companyRepository;
    private final CompanyQueryRepository companyQueryRepository;

    private final JwtTokenProvider jwtTokenProvider;

    public List<FindCompanyRes> findCompanies(String keyword, HttpServletRequest request){
        List<FindCompanyRes> companyResList = companyQueryRepository.findCompanyByKeyword(keyword);

        // header에서 token 추출
        String token = jwtTokenProvider.resolveToken(request);
        if(token == null){ // null이면 토큰 없음 -> 모두 관심여부 false로 반환
            return companyResList;
        }

        // Access Token 검증
        if (!jwtTokenProvider.validateToken(token)) {
            throw new CustomException(ACCESS_TOKEN_INVALID);
        }

        // Access Token 에서 User id를 가져옴
        Authentication authentication = jwtTokenProvider.getAuthentication(token);
        String loginId = authentication.getName();

        // TODO: user login id로 관심 기업 설정해둔 거 확인해서 true로 바꿔서 반환

        return companyResList;
    }

    public FindCompanyDetailRes findCompany(Long companyId, HttpServletRequest request){
        Optional<Company> companyRes = companyRepository.findById(companyId);

        if(companyRes.isEmpty()){
            throw new CustomException(COMPANY_NOT_FOUND);
        }

        FindCompanyDetailRes findCompanyDetailRes = FindCompanyDetailRes.from(companyRes.get());

        // header에서 token 추출
        String token = jwtTokenProvider.resolveToken(request);
        if(token == null){ // null이면 토큰 없음 -> 모두 관심여부 false로 반환
            return findCompanyDetailRes;
        }

        // Access Token 검증
        if (!jwtTokenProvider.validateToken(token)) {
            throw new CustomException(ACCESS_TOKEN_INVALID);
        }

        // Access Token 에서 User id를 가져옴
        Authentication authentication = jwtTokenProvider.getAuthentication(token);
        String loginId = authentication.getName();

        // TODO: user login id로 관심 기업 설정해둔 거 확인해서 true로 바꿔서 반환

        // TODO: funding api 개발 후 fundingsNumber, participantsNumber, amount 설정

        return findCompanyDetailRes;
    }
}