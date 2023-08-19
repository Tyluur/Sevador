package com.sevador.content.chardesign;

public class DefaultDesign {

    private DefaultSubDesign[] subDesigns;
    private int[][] colours;

    public DefaultDesign(DefaultSubDesign[] subDesigns, int[][] colours) {
        this.setSubDesigns(subDesigns);
        this.setColours(colours);
    }

    /**
	 * @return the subDesigns
	 */
	public DefaultSubDesign[] getSubDesigns() {
		return subDesigns;
	}

	/**
	 * @param subDesigns the subDesigns to set
	 */
	public void setSubDesigns(DefaultSubDesign[] subDesigns) {
		this.subDesigns = subDesigns;
	}

	/**
	 * @return the colours
	 */
	public int[][] getColours() {
		return colours;
	}

	/**
	 * @param colours the colours to set
	 */
	public void setColours(int[][] colours) {
		this.colours = colours;
	}

	public static class DefaultSubDesign {

        private int[][] looks;

        public DefaultSubDesign(int[][] looks) {
            this.setLooks(looks);
        }

		/**
		 * @return the looks
		 */
		public int[][] getLooks() {
			return looks;
		}

		/**
		 * @param looks the looks to set
		 */
		public void setLooks(int[][] looks) {
			this.looks = looks;
		}

    }

}
