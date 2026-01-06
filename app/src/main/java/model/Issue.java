package model;

/**
 * Class used to simulate issues on a line.
 */
public class Issue {
  private int counter;

  /** Setting initial countdown timer to 10. */
  public Issue() {
    this.counter = 0;
  }

  /** decrements countdown timer by one. */
  public void decrementCounter() {
    this.counter--;
  }

  /** Inject issue by setting countdown timer to 10. */
  public void createIssue() {
    this.counter = 10;
  }

  /**
   * Gets the current counter value.
   *
   * @return int of the current counter value.
   */
  public int getCounter() {
    return counter;
  }

  /**
   * Checks if issue is resolved by checking counter.
   *
   * @return true if issue is resolved else false
   */
  public boolean isIssueResolved() {
    return this.counter == 0;
  }
}
