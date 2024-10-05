package osm.surveyor.task.city;

import osm.surveyor.task.task.TaskException;

/**
 * 406 Not Acceptable
 * "ACCEPTIONGではないため予約できません"
 * 	・サーバ側が受付不可能な値（ファイルの種類など）であり提供できない状態
 * 	・トランザクションはリバースされる
 */
public class NotAcceptableException extends TaskException {
	private static final long serialVersionUID = 1L;
	
	public NotAcceptableException(String errorMessage) {
		super(errorMessage);
	}
}
