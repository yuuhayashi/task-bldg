package osm.surveyor.task.city.model;

public enum Status {
    PREPARATION, 	// 1: preparation 準備中	"X"
    ACCEPTING,		// 2: Accepting 受付中		"1"
    EDITING,		// 4: 編集待ち				"2"
    RESERVED,		// 3: Reserved 予約済み		"3"
    NG,				// 5: 検証(NG)				"0"
    OK				// 6: 完了					"4"
}
