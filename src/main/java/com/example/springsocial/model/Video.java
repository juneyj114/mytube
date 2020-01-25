package com.example.springsocial.model;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Transient;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.example.springsocial.util.LikeStatus;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
public class Video {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Long id;
	
	public String title;
	
	@Column(columnDefinition = "TEXT")
	public String content;
	
	@Column(length = 500)
	public String url;
	
	public Boolean isPublic;
	
	@Column(columnDefinition = "BIGINT DEFAULT 0")
	public Long readCount;
	
	@OneToMany(mappedBy = "video", fetch = FetchType.EAGER ,cascade = CascadeType.ALL)
	@JsonIgnoreProperties({"user","video"})
	public List<Likes> likes = new ArrayList<Likes>();
	
	@ManyToOne
	@JoinColumn(name = "authorId")
	@JsonIgnoreProperties({"videos", "subscribing", "playList"})
	public User author;
	
	@CreationTimestamp
	public Timestamp createDate;
	
	@UpdateTimestamp
	public Timestamp updateDate;
	
	@Transient
	public String date;
	
	@Transient
	public int likeCount;
	
	@Transient
	public int unLikeCount;
	
	@Transient
	public LikeStatus like = LikeStatus.NOTHING;
	
	@Transient
	public boolean sub = false;
	
	public boolean getIsPublic() {
		return this.isPublic;
	}
	
}

