=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=
CIS 120 Game Project README
PennKey: ezwang
=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=

===================
=: Core Concepts :=
===================

- List the four core concepts, the features they implement, and why each feature
  is an approprate use of the concept. Incorporate the feedback you got after
  submitting your proposal.

  1. 2-D Arrays
        Used to represent the chessboard. Chess moves are reflected as index
        changes in the array. This is an appropriate data structure since the
        size of the array never changes, and looking up which piece is on a particular
        square is an easy to do operation.

  2. Collections
        Used in various places, such as to indicate a collection of moves (TreeSet)
        or to keep track of the history of the game (LinkedList). Particularly good
        for storing moves and locations, since checking if a move exists in a set
        and modifying sets are pretty frequently used operations. TreeSet is a good
        data structure for these requirements since it has O(log n) insert and O(log n)
        lookup.

  3. Inheritance/Subtyping Dynamic Dispatch
        Used for Packets and Pieces. When the server and client receive packets, they
        can rely on dynamic dispatch to perform the intended behavior for the packet,
        instead of doing a lot of casework. This is an appropriate use for representing
        chess pieces, since pieces have similar operations (ex: get all possible squares
        that this piece can move to) but different results depending on the type of
        the piece.

  4. Networking I/O
        Used for the multiplayer aspect of the game. Consists of a client server model,
        with the server doing matchmaking and passing data between 2 clients that are
        in a game. Implementing networking allows users to play the game when
        they are on different computers and when they are far away in real life.


=========================
=: Your Implementation :=
=========================

- Provide an overview of each of the classes in your code, and what their
  function is in the overall game.

    Networking
        Server: Creates ServerConnections when a new player joins.
        ServerConnection: Handles the networking on the server side.
        ClientConnection: Handles the networking on the client side.
        Packet: Used to transmit information over the network.
            PacketDisconnect
            PacketDraw
            PacketEnd
            PacketMove
            PacketNickname
            PacketStart
            PacketUndo
        PacketUtils: Provides methods for turning Java objects into strings
                     and vice versa.
    GUI
        IntroPanel: The screen containing the instructions and play buttons.
        GamePanel: A wrapper for the information and board.
        InfoPanel: A panel containing information about the game.
        BoardPanel: The chess board and pieces.
    Game: The entry point for the game.
        GameState: A data structure containing all the information in a chess game.
        Location: Represents a location on the board.
        Move: Represents a piece move.
        Pieces: Represent pieces on the chessboard.
            King
            Queen
            Bishop
            Knight
            Rook
            Pawn
        PieceCaptureCondition: Represents a piece that has special conditions for
                               capturing (used by king (cannot capture protected piece)
                               and pawn (can only capture diagonally)).
        PieceFirstMove: Keeps track of number of times a piece moved. Necessary for
                        castling (used by king and rook).

- Were there any significant stumbling blocks while you were implementing your
  game (related to your design, or otherwise)?

    There were a lot of special cases for castling/en passant. Adding unit tests
    helped to catch most of them.

- Evaluate your design. Is there a good separation of functionality? How well is
  private state encapsulated? What would you refactor, if given the chance?

    The functionality is separated into distinct components, with networking,
    game state, and the GUI grouped in separate classes. It is possible to create
    an isolated game state for unit testing purposes.

    Private state is encapsulated quite well, but there are still exposed variables
    that should be hidden. This is because "private" does not allow classes that
    extend the current class to access variables. I use the "protected" keyword,
    but this acts pretty much the same as "public" since the project is not in
    a package. If it was allowed to separate the components into different packages,
    then the protected keyword would work as intended.

    I would have used an enum to represent white/black instead of a boolean. This
    would make the code easier to read and reduce confusion about method arguments.

========================
=: External Resources :=
========================

- Cite any external resources (libraries, images, tutorials, etc.) that you may
  have used while implementing your game.

    Chess Piece Images from Wikipedia (CC 3.0 License)
    - https://upload.wikimedia.org/wikipedia/commons/thumb/4/42/Chess_klt45.svg/480px-Chess_klt45.svg.png
    - https://upload.wikimedia.org/wikipedia/commons/thumb/f/f0/Chess_kdt45.svg/480px-Chess_kdt45.svg.png
    - https://upload.wikimedia.org/wikipedia/commons/thumb/1/15/Chess_qlt45.svg/480px-Chess_qlt45.svg.png
    - https://upload.wikimedia.org/wikipedia/commons/thumb/4/47/Chess_qdt45.svg/480px-Chess_qdt45.svg.png
    - https://upload.wikimedia.org/wikipedia/commons/thumb/7/72/Chess_rlt45.svg/480px-Chess_rlt45.svg.png
    - https://upload.wikimedia.org/wikipedia/commons/thumb/f/ff/Chess_rdt45.svg/480px-Chess_rdt45.svg.png
    - https://upload.wikimedia.org/wikipedia/commons/thumb/b/b1/Chess_blt45.svg/480px-Chess_blt45.svg.png
    - https://upload.wikimedia.org/wikipedia/commons/thumb/9/98/Chess_bdt45.svg/480px-Chess_bdt45.svg.png
    - https://upload.wikimedia.org/wikipedia/commons/thumb/7/70/Chess_nlt45.svg/480px-Chess_nlt45.svg.png
    - https://upload.wikimedia.org/wikipedia/commons/thumb/e/ef/Chess_ndt45.svg/480px-Chess_ndt45.svg.png
    - https://upload.wikimedia.org/wikipedia/commons/thumb/4/45/Chess_plt45.svg/480px-Chess_plt45.svg.png
    - https://upload.wikimedia.org/wikipedia/commons/thumb/c/c7/Chess_pdt45.svg/480px-Chess_pdt45.svg.png

    Google GSON - https://github.com/google/gson
