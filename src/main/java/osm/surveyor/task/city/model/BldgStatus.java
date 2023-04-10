package osm.surveyor.task.city.model;

/**
 * TASKの操作ステータス
 * 	「準備中」
 * 	→ 「受付中」
 * 	→
 * @author hayashi
 */
public enum BldgStatus {
    INIT,		 	// 未編集
    NG,				// 検証(NG)
    DONE			// 完了
}
