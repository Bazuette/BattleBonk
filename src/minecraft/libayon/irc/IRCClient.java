package libayon.irc;

import libayon.utils.player.PlayerUtils;

public class IRCClient extends libayon.utils.IRCClient {

	private final String channel;

	private final String prefix;

	public IRCClient(String host, int port, String username, String channel) {
		super(host, port, username);

		this.channel = channel;
		this.prefix = "&dIRC&7> ";
	}

	public String getChannel() {
		return channel;
	}

	public String getPrefix() {
		return prefix;
	}

	@Override
	public void listener(String line) {
		if (!(isActive()))
			return;

		if (!(line.contains("!")) || !(line.contains(":")))
			return;

		String name = line.substring(1, line.indexOf("!"));
		String message = line.substring(line.lastIndexOf(":") + 1);

		PlayerUtils.sendMessage(String.format("%1$s&b%2$s&7: &e%3$s", prefix, name, message), false);
	}

	@Override
	public void onConnect() {
	}

	@Override
	public void exception(Exception e) {
	}

}