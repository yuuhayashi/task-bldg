package osm.surveyor.task.util;

import java.io.Serializable;
import java.util.UUID;

import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.IdentityGenerator;

public class UuidGenerator extends IdentityGenerator{
	
	private static final long serialVersionUID = 1L;

	/**
	 * UUIDを生成し返却する
	 */
	public Serializable generate(SharedSessionContractImplementor session, Object obj) {
        return UUID.randomUUID().toString();
    }
}
