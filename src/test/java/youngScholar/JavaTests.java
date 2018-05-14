package youngScholar;

import youngScholar.util.GenerateRandomSequence;
import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JavaTests extends Assert
{
    protected Logger logger = LoggerFactory.getLogger(getClass());

    @Test
    public void testRandom()
    {
        GenerateRandomSequence randomSequence = new GenerateRandomSequence();
        for (int i = 0; i < 100; i++)
        {
            logger.warn(randomSequence.getRandomUppercaseNumber());
        }
    }
}
