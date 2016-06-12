package me.desht.chesscraft.commands;

import me.desht.chesscraft.ChessValidate;
import me.desht.chesscraft.Messages;
import me.desht.chesscraft.chess.BoardView;
import me.desht.chesscraft.chess.BoardViewManager;
import me.desht.chesscraft.chess.ChessGame;
import me.desht.chesscraft.chess.ChessGameManager;
import me.desht.chesscraft.exceptions.ChessException;
import me.desht.dhutils.MiscUtil;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.util.List;

public class ArchiveCommand extends ChessAbstractCommand {

	public ArchiveCommand() {
		super("chess archive", 0, 1);
		setPermissionNode("chesscraft.commands.archive");
		setUsage(new String[] {
				"/chess archive",
				"/chess archive <game-name>",
				"/chess archive -this",
		});
	}

	@Override
	public boolean execute(Plugin plugin, CommandSender sender, String[] args) throws ChessException {
		ChessGame game;
		if (args.length >= 1) {
			if (args[0].equals("-this")) {
				notFromConsole(sender);
				Player player = (Player)sender;
				BoardView bv = BoardViewManager.getManager().partOfChessBoard(player.getLocation());
				if (bv == null) {
					throw new ChessException(Messages.getString("Designer.notOnBoard"));
				} else {
					game = bv.getGame();
				}
			} else {
				game = ChessGameManager.getManager().getGame(args[0]);
			}
		} else {
			notFromConsole(sender);
			game = ChessGameManager.getManager().getCurrentGame((Player) sender);
		}
		ChessValidate.notNull(game, Messages.getString("ChessCommandExecutor.noActiveGame"));

		File written = game.writePGN(false);
		MiscUtil.statusMessage(sender, Messages.getString("ChessCommandExecutor.PGNarchiveWritten", written.getName()));
		return true;
	}

	@Override
	public List<String> onTabComplete(Plugin plugin, CommandSender sender, String[] args) {
		if (args.length == 1) {
			return getGameCompletions(plugin, sender, args[0]);
		} else {
			showUsage(sender);
			return noCompletions(sender);
		}
	}
}

