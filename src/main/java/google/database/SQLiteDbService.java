package google.database;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import google.dao.ResultDao;
import google.model.Result;

@Service
class SQLiteDbService implements DbService {

	@Autowired
	private ResultDao resultDao;

	@Override
	public void saveResult(Result result) {
		resultDao.insert(result);
	}

	@Override
	public List<Result> findAllResults() {
		return resultDao.findAll();
	}

	@Override
	public Optional<Result> findResult(String query) {
		return resultDao.find(query);
	}

	@Override
	public void deleteAllResults() {
		resultDao.deleteAll();
	}

	@Override
	public void deleteResults(List<String> resultsToRemove) {
		resultsToRemove.stream().forEach(resultDao::delete);
	}

	@Override
	public void deleteResult(String query) {
		resultDao.delete(query);
	}

}
