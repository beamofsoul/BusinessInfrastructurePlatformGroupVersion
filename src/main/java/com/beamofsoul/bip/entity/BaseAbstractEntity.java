package com.beamofsoul.bip.entity;

import java.io.Serializable;
import java.io.StringWriter;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;

import org.springframework.format.annotation.DateTimeFormat;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * 基本
 */
@Data
@AllArgsConstructor
@MappedSuperclass
public abstract class BaseAbstractEntity implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 创建日期
	 */
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@Column(name = "CREATE_DATE", updatable = false)
	protected Date createDate;

	/**
	 * 修改日期
	 */
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@Column(name = "MODIFY_DATE")
	protected Date modifyDate;

	public BaseAbstractEntity() {
		this.createDate = this.modifyDate = new Date();
	}

	public String toString() {
		return JSONObject.toJSONString(this, SerializerFeature.DisableCircularReferenceDetect);
	}

	public String toXML() {
		String xmlString = null;
		try {
			StringWriter writer = new StringWriter();
			JAXBContext context = JAXBContext.newInstance(getClass());
			Marshaller marshaller = context.createMarshaller();
			marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);

			marshaller.marshal(this, writer);
			xmlString = writer.toString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return xmlString;
	}
}
