package osm.surveyor.task.city.model;

/**
 * マッパーの操作
 * @author hayashi
 *
 */
public enum Operation {
	NOP,			// ノーオペ
    RESERVE, 		// 担当登録
    CANCEL,			// 編集中止
    DONE,			// 編集完了
    VALID,			// 検証
    NG,				// (deprecated)検証済み
    OK,				// (deprecated)検証済み
    END,			// (deprecated)完了
    VIEW			// 参照
}
