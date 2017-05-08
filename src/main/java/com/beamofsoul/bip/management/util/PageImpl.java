package com.beamofsoul.bip.management.util;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.springframework.core.convert.converter.Converter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import lombok.NonNull;

public class PageImpl<T> implements Page<T>, Serializable {

	private static final long serialVersionUID = 1280680157698704303L;

	private final List<T> content = new ArrayList<T>();  
    private final Pageable pageable;  
    private final long total;
    
    public PageImpl(@NonNull List<T> content, Pageable pageable, long total) {  
        this.content.addAll(content);  
        this.total = total;  
        this.pageable = pageable;  
    }
    
    public PageImpl(List<T> content, Pageable pageable) {  
        this(content, pageable, content == null ? 0 : content.size());  
    }
    
    public PageImpl(List<T> content) {
        this(content, null, content == null ? 0 : content.size());  
    }  
	
	@Override
	public int getNumber() {
		return pageable == null ? 0 : pageable.getPageNumber();
	}

	@Override
	public int getSize() {
		return pageable == null ? 0 : pageable.getPageSize();
	}

	@Override
	public int getNumberOfElements() {
		return content.size();
	}

	@Override
	public List<T> getContent() {
		return Collections.unmodifiableList(content);
	}

	@Override
	public boolean hasContent() {
		 return !content.isEmpty();
	}

	@Override
	public Sort getSort() {
		return pageable == null ? null : pageable.getSort();
	}

	@Override
	public boolean isFirst() {
		return !hasPrevious();
	}

	@Override
	public boolean isLast() {
		return !hasNext();
	}

	@Override
	public boolean hasNext() {
		return getNumber() + 1 < getTotalPages();
	}

	@Override
	public boolean hasPrevious() {
		return getNumber() > 0;
	}

	@Override
	public Pageable nextPageable() {
		return hasNext() ? pageable.next() : null;
	}

	@Override
	public Pageable previousPageable() {
		return hasPrevious() ? pageable.previousOrFirst() : null;
	}

	@Override
	public Iterator<T> iterator() {
		return content.iterator();
	}

	@Override
	public int getTotalPages() {
		return getSize() == 0 ? 1 : (int) Math.ceil((double) total / (double) getSize());
	}

	@Override
	public long getTotalElements() {
		return total;
	}

	@Override
	public <S> Page<S> map(@NonNull Converter<? super T, ? extends S> converter) {
		List<S> result = new ArrayList<S>(content.size());
		for (T element : this) {
			result.add(converter.convert(element));
		}
		return new PageImpl<S>(result, pageable, total);
	}

	@Override
	public String toString() {
        return String.format("Page %s of %d containing %s instances", getNumber(), getTotalPages(), content.size() > 0 ? content.get(0).getClass().getName() : "UNKNOWN");
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {  
            return true;  
        }  
        if (!(obj instanceof PageImpl<?>)) {  
            return false;  
        }  
        PageImpl<?> that = (PageImpl<?>) obj;  
        boolean totalEqual = this.total == that.total;  
        boolean contentEqual = this.content.equals(that.content);  
        boolean pageableEqual = this.pageable == null ? that.pageable == null : this.pageable.equals(that.pageable);  
        return totalEqual && contentEqual && pageableEqual;
	}
	
	@Override
	public int hashCode() {
		int result = 17;  
        result = 31 * result + (int) (total ^ total >>> 32);  
        result = 31 * result + (pageable == null ? 0 : pageable.hashCode());  
        result = 31 * result + content.hashCode();  
        return result;
	}
}
