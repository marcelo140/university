package ex4;

import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;

public class Authenticator {
    private final AsyncLineBuffer buf;
    private final Chat chat;
    private boolean isAuthenticated = false;

    Authenticator(AsynchronousSocketChannel ch, Chat chat) {
        this.buf = new AsyncLineBuffer(ch);
        this.chat = chat;
    }

    public <A> void authenticate(final A value, CompletionHandler<Void, A> handler) {
        buf.read(null, new CompletionHandler<>() {
            @Override
            public void completed(String result, Object attachment) {
                if (result.equals("register")) {
                    handleRegistration(null, new CompletionHandler<>() {
                        @Override
                        public void completed(Boolean result, Object attachment) {
                            if (result.booleanValue()) {
                                handler.completed(null, value);
                            } else {
                                authenticate(value, handler);
                            }
                        }

                        @Override
                        public void failed(Throwable exc, Object attachment) {
                            exc.printStackTrace();
                        }
                    });
                }
                else if (result.equals("login")) {
                    handleLogin(null, new CompletionHandler<>() {
                        @Override
                        public void completed(Boolean result, Object attachment) {
                            if (result.booleanValue()) {
                                handler.completed(null, value);
                            } else {
                                authenticate(value, handler);
                            }
                        }

                        @Override
                        public void failed(Throwable exc, Object attachment) {
                            exc.printStackTrace();
                        }
                    });
                } else {
                    authenticate(value, handler);
                }
            }

            @Override
            public void failed(Throwable exc, Object attachment) {
                exc.printStackTrace();
            }
        });
    }

    private <A> void handleLogin(final A value, CompletionHandler<Boolean, A> handler) {
        buf.read(null, new CompletionHandler<String, Void>() {
            @Override
            public void completed(String result, Void attachment) {
                buf.read(result, new CompletionHandler<>() {
                    @Override
                    public void completed(String result, String username) {
                        String password = result;
                        boolean success = chat.login(username, password);

                        if (success) {
                            isAuthenticated = true;
                            handler.completed(true, value);
                            buf.write("Login successful", null, new CompletionHandler<>() {
                                @Override
                                public void completed(Void result, Object attachment) {

                                }

                                @Override
                                public void failed(Throwable exc, Object attachment) {

                                }
                            });
                        } else {
                            handler.completed(false, value);
                        }
                    }

                    @Override
                    public void failed(Throwable exc, String attachment) {
                        exc.printStackTrace();
                    }
                });
            }

            @Override
            public void failed(Throwable exc, Void attachment) {
                exc.printStackTrace();
            }
        });
    }

    private <A> void handleRegistration(final A value, CompletionHandler<Boolean, A> handler) {
        buf.read(null, new CompletionHandler<String, Void>() {
            @Override
            public void completed(String result, Void attachment) {
                buf.read(result, new CompletionHandler<>() {
                    @Override
                    public void completed(String result, String username) {
                       String password = result;
                       boolean success = chat.register(username, password);

                       if (success) {
                           isAuthenticated = true;
                           handler.completed(true, value);
                           buf.write("Registration successful", null, new CompletionHandler<>() {
                               @Override
                               public void completed(Void result, Object attachment) {

                               }

                               @Override
                               public void failed(Throwable exc, Object attachment) {

                               }
                           });
                       } else {
                           handler.completed(false, value);
                       }
                    }

                    @Override
                    public void failed(Throwable exc, String attachment) {
                        exc.printStackTrace();
                    }
                });
            }

            @Override
            public void failed(Throwable exc, Void attachment) {
               exc.printStackTrace();
            }
        });
    }

    public <A> void read(final A value, CompletionHandler<String, A> handler) {
        if (!isAuthenticated)
            return;

        buf.read(value, handler);
    }

    public <A> void write(String str, final A value, CompletionHandler<Void, A> handler) {
        if (!isAuthenticated)
            return;

        buf.write(str, value, handler);
    }
}