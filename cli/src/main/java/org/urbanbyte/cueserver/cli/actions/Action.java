package org.urbanbyte.cueserver.cli.actions;

/**
 * Defines actions that may be executed from the test CLI.
 * <p>
 * author: Chris Reising
 */
public interface Action
{
    /**
     * Gets a brief description of the action.
     *
     * @return Never {@code null}.
     */
    String getDescription();

    /**
     * Executes the example.
     */
    void executeAction();
}
