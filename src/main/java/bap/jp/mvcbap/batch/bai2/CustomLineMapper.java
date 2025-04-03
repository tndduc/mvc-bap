package bap.jp.mvcbap.batch.bai2;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;

public class CustomLineMapper<T> extends DefaultLineMapper<T> {
    private static final Logger logger = LoggerFactory.getLogger(CustomLineMapper.class);

    @Override
    public T mapLine(String line, int lineNumber) throws Exception {
	try {
	    return super.mapLine(line, lineNumber);
	} catch (Exception e) {
	    logger.error("Line {}. Error: {}", lineNumber, e.getMessage());
	    throw e;
	}
    }
}