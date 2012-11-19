package com.lyncode.oai.proxy.model.dao.api;

import java.util.List;

import com.lyncode.oai.proxy.model.entity.Repository;

public interface RepositoryDao {
	public int saveRepository(Repository repository);
	public List<Repository> getAll ();
	public List<Repository> selectByUserId(int userId);
	public void deleteRepository(Repository r);
}
