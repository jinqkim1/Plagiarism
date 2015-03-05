package com.kdars.HotCheetos.MapReduce;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.Writable;
import org.apache.hadoop.mapreduce.lib.db.DBWritable;

public class inputDBRecords implements Writable, DBWritable{
//	int index;
//	int docID;
	String term;
	int termFrequency;
	
	@Override
	public void readFields(ResultSet resultSet) throws SQLException {
		// TODO Auto-generated method stub
//		this.index = resultSet.getInt(1);
//		this.docID = resultSet.getInt(2);
//		this.term = resultSet.getString(3);
//		this.termFrequency = resultSet.getInt(4);
		
		this.term = resultSet.getString(1);
		this.termFrequency = resultSet.getInt(2);
	}

	@Override
	public void write(PreparedStatement stmt) throws SQLException {
		// TODO Auto-generated method stub
//		stmt.setInt(1, this.index);
//		stmt.setInt(2, this.docID);
//		stmt.setString(3, this.term);
//		stmt.setInt(4, this.termFrequency);
		
		stmt.setString(1, this.term);
		stmt.setInt(2, this.termFrequency);
	}

	@Override
	public void readFields(DataInput in) throws IOException {
		// TODO Auto-generated method stub
//		this.index = in.readInt();
//		this.docID = in.readInt();
//		this.term = Text.readString(in);
//		this.termFrequency = in.readInt();
		
		this.term = Text.readString(in);
		this.termFrequency = in.readInt();
	}

	@Override
	public void write(DataOutput out) throws IOException {
		// TODO Auto-generated method stub
//		out.writeInt(this.index);
//		out.writeInt(this.docID);
//		Text.writeString(out, this.term);
//		out.writeInt(this.termFrequency);
		
		Text.writeString(out, this.term);
		out.writeInt(this.termFrequency);
	}
	
	public String toString() {
//		return new String(this.index + " " + this.docID + " " + this.term + " " + this.termFrequency);
		return new String(this.term + " " + this.termFrequency);
	}
	
}
