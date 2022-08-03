package osm.surveyor.task.city.model;

import java.util.List;

import lombok.Builder;
import lombok.Getter;

/**
 * レスポンス用クラス。
 */
@Getter
@Builder
public class Tasks {

	private Result result; // 処理結果
	
	private List<Task> tasks; // データ
	
	@Getter
	@Builder
	public static class Result {
		private String message; // 処理結果メッセージ
		private int count; // データ件数
	}
}
