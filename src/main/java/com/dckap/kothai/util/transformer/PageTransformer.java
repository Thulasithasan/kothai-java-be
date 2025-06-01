package com.dckap.kothai.util.transformer;

import com.dckap.kothai.payload.response.PageDto;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

@Service
public class PageTransformer implements BaseTransformer<Page<?>, PageDto> {

	@Override
	public PageDto transform(Page<?> page) {
		PageDto pageDto = new PageDto();

		if (page == null || page.isEmpty()) {
			return pageDto;
		}

		pageDto.setCurrentPage(page.getNumber());
		pageDto.setTotalPages(page.getTotalPages());
		pageDto.setTotalItems(page.getTotalElements());
		return pageDto;
	}

}
