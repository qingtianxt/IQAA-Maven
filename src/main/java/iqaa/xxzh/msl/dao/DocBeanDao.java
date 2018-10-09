package iqaa.xxzh.msl.dao;

import iqaa.xxzh.msl.bean.DocBean;

public interface DocBeanDao extends Dao<DocBean> {

	DocBean findById(Long id);

	Integer totalCount();

	DocBean findByName(String docName);

}
