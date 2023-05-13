package osm.surveyor.task.city.model;

public enum Status {
    PREPARATION, 	// preparation 準備中	"X"
    ACCEPTING,		// Accepting 受付中		"1"
    EDITING,		// 編集待ち				"2"
    NG,				// 検証(NG)				"0"
    OK				// 完了					"4"
}
