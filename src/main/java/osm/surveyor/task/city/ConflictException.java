package osm.surveyor.task.city;

import osm.surveyor.task.task.TaskException;

/**
 * 409 Conflict
 * "タスクが変更されたため更新できません"
 * 	・サーバに既に存在しているデータが競合しているためリクエストを完了できない
 * 	・トランザクションはリバースされる
 */
public class ConflictException extends TaskException {
	private static final long serialVersionUID = 1L;
	
	public ConflictException(String errorMessage) {
		super(errorMessage);
	}

}
