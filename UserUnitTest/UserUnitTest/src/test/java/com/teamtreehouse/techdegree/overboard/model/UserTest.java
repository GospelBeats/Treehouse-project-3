package com.teamtreehouse.techdegree.overboard.model;

import com.teamtreehouse.techdegree.overboard.exc.AnswerAcceptanceException;
import com.teamtreehouse.techdegree.overboard.exc.VotingException;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.junit.Assert.*;

public class UserTest {
    private Board tBoard;
    private User tQuestionerUser, tAnswererUser, tOtherUser;
    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Before
    public void setUp() throws Exception {
        tBoard = new Board("board_topic");
        tQuestionerUser = tBoard.createUser("questioner_user");
        tAnswererUser = tBoard.createUser("answerer_user");
        tOtherUser = tBoard.createUser("other_user");
    }

    @Test
    public void questionersReputationGoesUpByFiveWhenQuestionIsUpVoted()
            throws Exception {
        int initialQuestionerReputation = tQuestionerUser.getReputation();
        Question qn = tQuestionerUser.askQuestion("question?");
        tOtherUser.upVote(qn);
        assertEquals(initialQuestionerReputation + 5,
                tQuestionerUser.getReputation());
    }

    @Test
    public void answererReputationGoesUpByTenIfAnswerIsUpVoted()
            throws Exception {
        int initialAnswererReputation = tAnswererUser.getReputation();
        Question qn = tQuestionerUser.askQuestion("question?");
        Answer ans = tAnswererUser.answerQuestion(qn,"answer.");
        tQuestionerUser.upVote(ans);
        assertEquals(initialAnswererReputation + 10,
                tAnswererUser.getReputation());
    }

    @Test
    public void havingAnswerAcceptedGivesAnswererPlusFifteenRep()
            throws Exception {
        int initialAnswererReputation = tAnswererUser.getReputation();
        Question qn = tQuestionerUser.askQuestion("question?");
        Answer ans = tAnswererUser.answerQuestion(qn,"answer.");
        tQuestionerUser.acceptAnswer(ans);
        assertEquals(initialAnswererReputation + 15,
                tAnswererUser.getReputation());
    }


    @Test
    public void whenQuestionerDownVotesHisQuestionVotingExceptionIsThrown()
            throws Exception {
        Question qn = tQuestionerUser.askQuestion("question?");
        expectedException.expect(VotingException.class);
        tQuestionerUser.downVote(qn);
    }

    @Test
    public void whenQuestionerUpvotesHisQuestionVotingExceptionIsThrown()
            throws Exception {
        Question qn = tQuestionerUser.askQuestion("question?");
        expectedException.expect(VotingException.class);
        tQuestionerUser.upVote(qn);
    }

    @Test
    public void whenQuestionerDownVotesHisAnswerVotingExceptionIsThrown()
            throws Exception {
        Question qn = tQuestionerUser.askQuestion("question?");
        Answer ans = tQuestionerUser.answerQuestion(qn, "answer.");
        expectedException.expect(VotingException.class);
        tQuestionerUser.downVote(ans);
    }

    @Test
    public void whenQuestionerUpvotesHisAnswerVotingExceptionIsThrown()
            throws Exception {
        Question qn = tQuestionerUser.askQuestion("question?");
        Answer ans = tQuestionerUser.answerQuestion(qn, "answer.");
        expectedException.expect(VotingException.class);
        tQuestionerUser.upVote(ans);
    }

    @Test
    public void whenOtherUserIsTryingToAcceptAnswerAnswerAcceptanceExceptionIsThrown()
            throws Exception {
        Question qn = tQuestionerUser.askQuestion("question?");
        Answer ans = tAnswererUser.answerQuestion(qn,"answer.");
        expectedException.expect(AnswerAcceptanceException.class);
        tAnswererUser.acceptAnswer(ans);
    }

    @Test
    public void questionerAcceptingAnswerToHisQuestionSetsAnswerIsAccepted()
            throws Exception {
        Question qn = tQuestionerUser.askQuestion("question?");
        Answer ans = tAnswererUser.answerQuestion(qn,"answer.");
        tQuestionerUser.acceptAnswer(ans);
        assertTrue(ans.isAccepted());
    }

    @Test
    public void downVotingQuestionDoesNotAffectQuestionerReputation()
            throws Exception {
        int initialQuestionerReputation = tQuestionerUser.getReputation();
        Question qn = tQuestionerUser.askQuestion("question?");
        tOtherUser.downVote(qn);
        assertEquals(initialQuestionerReputation,
                tQuestionerUser.getReputation());
    }

    @Test
    public void downVotingAnswerReducesAnswererReputationByMinusOne() {
        int initialAnswererReputation = tAnswererUser.getReputation();
        Question qn = tQuestionerUser.askQuestion("question?");
        Answer ans = tAnswererUser.answerQuestion(qn,"answer.");
        tQuestionerUser.downVote(ans);
        assertEquals(initialAnswererReputation - 1,
                tAnswererUser.getReputation());
    }
}