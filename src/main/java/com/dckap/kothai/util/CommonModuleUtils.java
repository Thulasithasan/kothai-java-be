package com.dckap.kothai.util;

import org.apache.commons.lang3.RandomStringUtils;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class CommonModuleUtils {

	public static final int MAX_USER_CHUNK_SIZE = 100;

	private CommonModuleUtils() {
		throw new UnsupportedOperationException("Utility class");
	}

	public static <T> List<List<T>> chunkData(List<T> dataList) {
		List<List<T>> chunkedList = new ArrayList<>();

		if (dataList != null && !dataList.isEmpty()) {
			int arrayLength = (int) Math.ceil((double) dataList.size() / MAX_USER_CHUNK_SIZE);
			for (int index = 0; index < arrayLength; index++) {
				int start = index * MAX_USER_CHUNK_SIZE;
				int end = Math.min(start + MAX_USER_CHUNK_SIZE, dataList.size());
				chunkedList.add(dataList.subList(start, end));
			}
		}

		return chunkedList;
	}

	public static <T> boolean isListNotNullAndNotEmpty(List<T> anyList) {
		return anyList != null && !anyList.isEmpty();
	}

	public static boolean isValidFloat(String floatStr) {
		if (floatStr != null && !floatStr.isBlank()) {
			try {
				Float.parseFloat(floatStr);
			}
			catch (NumberFormatException e) {
				return false;
			}
			return true;
		}
		else {
			return false;
		}
	}

	public static float calculateHoursBetweenEpochMillis(long start, long end) {
		return (float) (end - start) / (1000 * 60 * 60);
	}

	public static DayOfWeek getDayOfWeek(LocalDate date) {
		return date.getDayOfWeek();
	}

	public static boolean validateStartDateAndEndDate(LocalDate startDate, LocalDate endDate) {
		int currentYear = DateTimeUtils.getCurrentYear();
		return startDate.getYear() < currentYear - 1 || endDate.getYear() < currentYear - 1
				|| startDate.isAfter(endDate);
	}

	public static String encodeToBase64(String password) {
		return Base64.getEncoder().encodeToString(password.getBytes());
	}

	public static String generateSecureRandomPassword() {
		String upperCaseLetters = RandomStringUtils.random(2, 65, 90, true, true);
		String lowerCaseLetters = RandomStringUtils.random(2, 97, 122, true, true);
		String numbers = RandomStringUtils.randomNumeric(2);
		String specialChar = RandomStringUtils.random(2, 33, 47, false, false);
		String totalChars = RandomStringUtils.randomAlphanumeric(2);
		String combinedChars = upperCaseLetters.concat(lowerCaseLetters)
			.concat(numbers)
			.concat(specialChar)
			.concat(totalChars);
		List<Character> pwdChars = combinedChars.chars().mapToObj(c -> (char) c).collect(Collectors.toList());
		Collections.shuffle(pwdChars);
		return pwdChars.stream().collect(StringBuilder::new, StringBuilder::append, StringBuilder::append).toString();
	}

}
