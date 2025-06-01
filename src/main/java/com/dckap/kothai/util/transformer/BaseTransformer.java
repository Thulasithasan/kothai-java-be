package com.dckap.kothai.util.transformer;

public interface BaseTransformer<T, I> {

	I transform(T type);

}
