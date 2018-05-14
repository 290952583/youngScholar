package youngScholar.model;

import java.util.ArrayList;
import java.util.List;

public interface ModelFromEntityList<F, T extends ModelFromEntityList<F, T>> extends ModelFromEntity<F, T>
{
	default List<T> fromEntityList(List<F> fs)
	{
		Class<T> tClass = (Class<T>) this.getClass();
		List<T> output = new ArrayList<>();
		try
		{
			for (F f : fs)
			{
				output.add(tClass.newInstance().fromEntity(f));
			}
		}
		catch (ReflectiveOperationException e)
		{
		}
		return output;
	}
}
