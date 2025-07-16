package channelAlpha.lang;

public class Promise<TargetType> {
	public TargetType promise;
	public void waitProimse() { try { wait(); } catch (InterruptedException e) { e.printStackTrace(); } }
}
