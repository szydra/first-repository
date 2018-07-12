package google.finder;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import google.database.DbService;
import google.model.Result;

@Service
class ResultService implements FinderService {

	private ConcurrentCache cache = new ConcurrentCache();

	@Autowired
	private DbService dbService;

	@Autowired
	private GoogleConnector connector;

	@Override
	public void findAndSave(String query) {
		Result result = dbService.findResult(query)
				.orElseGet(() -> findAndSaveNewResult(query));
		cache.add(result);
	}

	@Override
	public List<Result> getAllResults() {
		List<Result> results = cache.getWholeCache();
		Collections.sort(results, (a, b) -> b.getNumberOfResults()
				.compareTo(a.getNumberOfResults()));
		return results;
	}

	private Result findAndSaveNewResult(String query) {
		Result result = new Result(query);
		result.setNumberOfResults(connector.getNumberOfResults(query));
		result.setDate(LocalDate.now());
		dbService.saveResult(result);
		return result;
	}

}
