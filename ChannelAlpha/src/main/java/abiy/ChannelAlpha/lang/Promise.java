package abiy.ChannelAlpha.lang;

public class Promise<TargetType> {
	public TargetType promise; // The actual promise object of generic type
	public void waitProimse() { try { wait(); } catch (InterruptedException e) { e.printStackTrace(); } } // Wait for promise completion (note: typo in method name)
}
