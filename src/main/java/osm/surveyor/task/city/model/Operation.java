package osm.surveyor.task.city.model;

/**
 * マッパーの操作
 * @author hayashi
 *
 */
public enum Operation {
	NOP,			// ノーオペ
    RESERVE, 		// 編集者登録
    CANCEL,			// 編集取消
    NG,				// 編集[NG]
    OK,			// 編集完了
    VIEW,			// 参照
    RESERVE_CANCEL	// 予約取消
}
