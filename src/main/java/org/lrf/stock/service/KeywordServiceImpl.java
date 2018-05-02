package org.lrf.stock.service;

import org.lrf.stock.entity.Code;
import org.lrf.stock.repository.CodeRepository;
import org.lrf.stock.util.Keys;
import org.lrf.stock.util.NumberUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component("keywordService")
public class KeywordServiceImpl implements KeywordService{

	@Autowired
	private CodeRepository codeRepository;
	
	public String getCodeFromKeyword(String keyWord) {

		if(!NumberUtil.isNumericzidai(keyWord)) {
			Code codeEntity = null;
			if((codeEntity = codeRepository.getCodeByName(keyWord)) == null) {
				throw new RuntimeException(Keys.NO_STOCK+" keyword is "+keyWord);
			}else {
				return codeEntity.getCode();
			}
		}else {
			if(codeRepository.getCodeEntityByCode(keyWord) == null) {
				throw new RuntimeException(Keys.NO_STOCK+" keyword is "+keyWord);
			}else {
				return keyWord;
			}
		}
	}
}
