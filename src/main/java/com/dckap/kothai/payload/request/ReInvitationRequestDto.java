package com.dckap.kothai.payload.request;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ReInvitationRequestDto {

	private List<Long> ids;

}
