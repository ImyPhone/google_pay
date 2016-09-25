package com.itcast.googlepay.view.flyoutin;

import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.LinearInterpolator;
import android.view.animation.ScaleAnimation;

public class AnimationUtil {

	private static final long	MEDIUM	= 500;

	/** ����һ������Ŵ�Ķ��� */
	public static Animation createZoomInNearAnim() {
		AnimationSet ret;
		Animation anim;
		ret = new AnimationSet(false);
		// ����һ������Ķ���
		anim = new AlphaAnimation(0f, 1f);
		anim.setDuration(MEDIUM);
		anim.setInterpolator(new LinearInterpolator());
		ret.addAnimation(anim);
		// ����һ���Ŵ�Ķ���
		anim = new ScaleAnimation(0, 1, 0, 1, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
		anim.setDuration(MEDIUM);
		anim.setInterpolator(new DecelerateInterpolator());
		ret.addAnimation(anim);
		return ret;
	}

	/** ����һ�������Ŵ�Ķ��� */
	public static Animation createZoomInAwayAnim() {
		AnimationSet ret;
		Animation anim;
		ret = new AnimationSet(false);
		// ����һ�������Ķ���
		anim = new AlphaAnimation(1f, 0f);
		anim.setDuration(MEDIUM);
		anim.setInterpolator(new DecelerateInterpolator());
		ret.addAnimation(anim);
		// ����һ���Ŵ�Ķ���
		anim = new ScaleAnimation(1, 3, 1, 3, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
		anim.setDuration(MEDIUM);
		anim.setInterpolator(new DecelerateInterpolator());
		ret.addAnimation(anim);
		return ret;
	}

	/** ����һ��������С�Ķ��� */
	public static Animation createZoomOutNearAnim() {
		AnimationSet ret;
		Animation anim;
		ret = new AnimationSet(false);
		// ����һ������Ķ���
		anim = new AlphaAnimation(0f, 1f);
		anim.setDuration(MEDIUM);
		anim.setInterpolator(new LinearInterpolator());
		ret.addAnimation(anim);
		// ����һ����С�Ķ���
		anim = new ScaleAnimation(3, 1, 3, 1, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
		anim.setDuration(MEDIUM);
		anim.setInterpolator(new DecelerateInterpolator());
		ret.addAnimation(anim);
		return ret;
	}

	/** ����һ��������С�Ķ��� */
	public static Animation createZoomOutAwayAnim() {
		AnimationSet ret;
		Animation anim;
		ret = new AnimationSet(false);
		// ����һ�������Ķ���
		anim = new AlphaAnimation(1f, 0f);
		anim.setDuration(MEDIUM);
		anim.setInterpolator(new DecelerateInterpolator());
		ret.addAnimation(anim);
		// ����һ����С�Ķ���
		anim = new ScaleAnimation(1, 0, 1, 0, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
		anim.setDuration(MEDIUM);
		anim.setInterpolator(new DecelerateInterpolator());
		ret.addAnimation(anim);
		return ret;
	}

	/** ����һ������Ŵ�Ķ��� */
	public static Animation createPanInAnim(float degree) {
		AnimationSet ret;
		Animation anim;
		ret = new AnimationSet(false);
		// ����һ�����붯��
		anim = new AlphaAnimation(0f, 1f);
		anim.setDuration(MEDIUM);
		anim.setInterpolator(new LinearInterpolator());
		ret.addAnimation(anim);
		// ����һ���Ŵ󶯻�
		final float pivotX = (float) (1 - Math.cos(degree)) / 2;
		final float pivotY = (float) (1 + Math.sin(degree)) / 2;

		anim = new ScaleAnimation(0.8f, 1, 0.8f, 1, Animation.RELATIVE_TO_SELF, pivotX, Animation.RELATIVE_TO_SELF,
				pivotY);
		anim.setDuration(MEDIUM);
		anim.setInterpolator(new DecelerateInterpolator());
		ret.addAnimation(anim);

		return ret;
	}

	/** ����һ��������С�Ķ��� */
	public static Animation createPanOutAnim(float degree) {
		AnimationSet ret;
		Animation anim;
		ret = new AnimationSet(false);
		// ����һ����������
		anim = new AlphaAnimation(1f, 0f);
		anim.setDuration(MEDIUM);
		anim.setInterpolator(new DecelerateInterpolator());
		ret.addAnimation(anim);
		// ����һ����С����
		final float pivotX = (float) (1 + Math.cos(degree)) / 2;
		final float pivotY = (float) (1 - Math.sin(degree)) / 2;
		anim = new ScaleAnimation(1, 0.8f, 1, 0.8f, Animation.RELATIVE_TO_SELF, pivotX, Animation.RELATIVE_TO_SELF,
				pivotY);
		anim.setDuration(MEDIUM);
		anim.setInterpolator(new DecelerateInterpolator());
		ret.addAnimation(anim);

		return ret;
	}
}
